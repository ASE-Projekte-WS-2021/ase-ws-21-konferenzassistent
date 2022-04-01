package com.example.myapplication;

import static com.example.myapplication.ChecklistPreset.convertToChecklistDatabaseEntry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.checklist.ChecklistItem;
import com.example.myapplication.data.RoomDB;
import com.example.myapplication.data.presets.checklist.ChecklistPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownItemData;
import com.example.myapplication.data.presets.countdown.CountdownParentData;
import com.example.myapplication.data.presets.countdown.CountdownParentWithItemData;
import com.example.myapplication.data.presets.countdown.CountdownPresetData;
import com.example.myapplication.data.presets.countdown.CountdownPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownPresetWithParentData;
import com.example.myapplication.meetingwizard.MeetingWizardActivity;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.onboarding.OnboardingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public MainActivity.FilterButtonListener filterButtonListener;
    private TextView actionBarText;
    private CreateMeetingBottomSheetAdapter meetingAdapter;

    public void setOnFilterButtonClickedListener(MainActivity.FilterButtonListener filterButtonListener) {
        this.filterButtonListener = filterButtonListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Disables Night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Hide the action bar, we only want to show it once the user scrolls down
        Objects.requireNonNull(getSupportActionBar()).hide();

        setUpMenu();

        // Check if Database has default values
        checkDatabaseEntries();

        // Set the Button to Visible on the Home Screen
        findViewById(R.id.main_activity_filter_button).setVisibility(View.VISIBLE);

        // Show Onboarding if app opened for the first time
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        boolean appOpenedFirstTime = preferences.getBoolean(getString(R.string.app_opened_first_time_key), true);
        if (appOpenedFirstTime) {
            preferences.edit().putBoolean(getString(R.string.app_opened_first_time_key), false).apply();
            startActivity(new Intent(this, OnboardingActivity.class));
        }
        // startActivity(new Intent(this, OnboardingActivity.class));

    }

    // Setup the Bottom Menu
    private void setUpMenu() {
        // Get the Navigation View and the Navigation Controller
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);

        // Setup Navigation Controller
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configure App Bar to Change
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.miStartseite, R.id.miVerlauf, R.id.miTeilnehmerverwaltung, R.id.miPresets).build();

        // Setup the App Bar with the Navigation Controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // initialize the custom action bar header
        actionBarText = findViewById(R.id.main_bar_title);

        // add listener for fragment change
        NavController.OnDestinationChangedListener listener =
                ((controller, navDestination, bundle) -> rebuildActionBar(navController));
        navController.addOnDestinationChangedListener(listener);

        findViewById(R.id.main_activity_filter_button).setOnClickListener(view -> {
            if (filterButtonListener != null) {
                filterButtonListener.onFilterButtonClicked();
            }
        });
    }

    // Suppress lint, as error is only thrown in Github Actions
    // Potientially gradle bug, see similar: https://stackoverflow.com/questions/41150995/appcompatactivity-oncreate-can-only-be-called-from-within-the-same-library-group
    @SuppressLint("RestrictedApi")
    // Edits the custom actionbar for every Fragment
    private void rebuildActionBar(NavController navController) {
        actionBarText.setText(Objects.requireNonNull(
                Objects.requireNonNull(getSupportActionBar()).getTitle()).toString().toUpperCase());


        // Check if the destination is the Verlauf Fragmnet
        if (!Objects.requireNonNull(navController.getCurrentDestination()).getDisplayName()
                .equals("com.example.myapplication:id/miVerlauf")) {
            findViewById(R.id.main_activity_filter_button).setVisibility(View.GONE);
        } else
            findViewById(R.id.main_activity_filter_button).setVisibility(View.VISIBLE);
    }

    // Opens the Meeting Wizard
    public void openMeetingWizard(View view) {
        // creates a Bottom sheet to create a meeting
        CreateMeetingBottomSheetAdapter createMeetingBottomSheetAdapter = new CreateMeetingBottomSheetAdapter();
        meetingAdapter = createMeetingBottomSheetAdapter;
        createMeetingBottomSheetAdapter.show(getSupportFragmentManager(), createMeetingBottomSheetAdapter.getTag());
    }

    public CreateMeetingBottomSheetAdapter getMeetingAdapter() {
        return meetingAdapter;
    }

    // Start the Meeting Creation Wizard
    public void startMeetingWizard(String title, String location, CountdownPresetPair pair, ChecklistPresetPair checklistPresetPair) {
        Intent intent = new Intent(this, MeetingWizardActivity.class);
        ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> object =
                CountdownPreset.convertToAdvancedCountdownList(RoomDB.getInstance(getBaseContext()), pair);

        ArrayList<ChecklistItem> items =
                ChecklistPreset.convertToChecklistItems(checklistPresetPair);
        // Give it the title and location
        intent.putExtra("meeting_wizard_title", title);
        intent.putExtra(MeetingWizardActivity.MEETING_LOCATION, location);
        intent.putExtra(MeetingWizardActivity.MEETING_COUNTDOWN, object);
        intent.putExtra(MeetingWizardActivity.MEETING_CHECKLIST, items);
        startActivity(intent);
    }

    private void checkDatabaseEntries() {
        // Load Countdowns Objects
        RoomDB database = RoomDB.getInstance(getBaseContext());
        List<CountdownPresetPair> d;
        d = database.countdownPresetWIthParentDao().getCountdowns();

        // If no preset exists create standard countdown
        if (d.size() < 1) {
            createStandard(database);
        }

        List<ChecklistPresetPair> checklistPresetPairs;
        checklistPresetPairs = database.checklistPresetWithItemDao().getPresets();

        if (checklistPresetPairs.size() < 1) {
            createStandardChecklist(database);
        }
    }

    private void createStandardChecklist(RoomDB database) {
        ArrayList<ChecklistItem> items = new ArrayList<>();
        items.add(new ChecklistItem("Desinfektionsmittel bereit"));
        items.add(new ChecklistItem("3G-Regelung o.ä. geprüft"));
        items.add(new ChecklistItem("Masken / Plexiglas geprüft"));
        items.add(new ChecklistItem("Abstände gewährleistet"));

        ChecklistPreset preset = new ChecklistPreset("Standard", items, 1);
        convertToChecklistDatabaseEntry(database, preset);
    }

    // TODO: simplify
    private void createStandard(RoomDB database) {

        CountdownPresetData preset = new CountdownPresetData();
        preset.setTitle("Standard");

        CountdownParentData parentData1 = new CountdownParentData();
        parentData1.setTitle("Lüftungs Timer");

        CountdownParentData parentData2 = new CountdownParentData();
        parentData2.setTitle("AbstandsTimer");

        CountdownItemData countdownItemData1 = new CountdownItemData();
        countdownItemData1.setCountdown((long) 15);
        countdownItemData1.setDescription("Fenster sollte geöffnet sein");

        CountdownItemData countdownItemData2 = new CountdownItemData();
        countdownItemData2.setCountdown((long) 10);
        countdownItemData2.setDescription("Fenster sollte geschlossen sein");

        CountdownItemData countdownItemData3 = new CountdownItemData();
        countdownItemData3.setCountdown((long) 30);

        long presetId = database.countdownPresetDao().insert(preset);

        long parent1Id = database.countdownParentDao().insert(parentData1);
        long parent2Id = database.countdownParentDao().insert(parentData2);

        long child1Id = database.countdownItemDao().insert(countdownItemData1);
        long child2Id = database.countdownItemDao().insert(countdownItemData2);
        long child3Id = database.countdownItemDao().insert(countdownItemData3);

        CountdownPresetWithParentData standardData1 = new CountdownPresetWithParentData();
        standardData1.setPresetID((int) presetId);
        standardData1.setCountdownParentID((int) parent1Id);

        CountdownPresetWithParentData standardData2 = new CountdownPresetWithParentData();
        standardData2.setPresetID((int) presetId);
        standardData2.setCountdownParentID((int) parent2Id);

        database.countdownPresetWIthParentDao().insert(standardData1);
        database.countdownPresetWIthParentDao().insert(standardData2);

        CountdownParentWithItemData countdownParentWithItemData1 = new CountdownParentWithItemData();
        countdownParentWithItemData1.setCountdownParentID((int) parent1Id);
        countdownParentWithItemData1.setCountdownItemID((int) child1Id);

        CountdownParentWithItemData countdownParentWithItemData2 = new CountdownParentWithItemData();
        countdownParentWithItemData2.setCountdownParentID((int) parent1Id);
        countdownParentWithItemData2.setCountdownItemID((int) child2Id);

        CountdownParentWithItemData countdownParentWithItemData3 = new CountdownParentWithItemData();
        countdownParentWithItemData3.setCountdownParentID((int) parent2Id);
        countdownParentWithItemData3.setCountdownItemID((int) child3Id);

        database.countdownParentWIthItemDao().insert(countdownParentWithItemData1);
        database.countdownParentWIthItemDao().insert(countdownParentWithItemData2);
        database.countdownParentWIthItemDao().insert(countdownParentWithItemData3);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public interface FilterButtonListener {
        void onFilterButtonClicked();
    }
}