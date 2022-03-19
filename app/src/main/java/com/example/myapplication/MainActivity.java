package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.meetingwizard.MeetingWizardActivity;
import com.example.myapplication.onboarding.OnboardingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private TextView actionBarText;
    private CreateMeetingBottomSheetAdapter meetingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Disables Night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Hide the action bar, we only want to show it once the user scrolls down
        Objects.requireNonNull(getSupportActionBar()).hide();

        setUpMenu();

        // Show Onboarding if app opened for the first time
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        boolean appOpenedFirstTime = preferences.getBoolean(getString(R.string.app_opened_first_time_key),true);
        if (appOpenedFirstTime) {
            preferences.edit().putBoolean(getString(R.string.app_opened_first_time_key),false).apply();
            startActivity(new Intent(this, OnboardingActivity.class));
        }
        // startActivity(new Intent(this, OnboardingActivity.class));
    }

    // Setup the Bottom Menu
    private void setUpMenu(){
        // Get the Navigation View and the Navigation Controller
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);


        // Setup Navigation Controller
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configure App Bar to Change
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.miHome, R.id.miSettings).build();

        // Setup the App Bar with the Navigation Controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // initialize the custom action bar header
        actionBarText = findViewById(R.id.main_bar_title);

        // add listener for fragment change
        NavController.OnDestinationChangedListener listener =
                ((controller, navDestination, bundle) -> rebuildActionBar());
        navController.addOnDestinationChangedListener(listener);

    }

    // Edits the custom actionbar for every Fragment
    private void rebuildActionBar(){
        actionBarText.setText(Objects.requireNonNull(getSupportActionBar()).getTitle().toString().toUpperCase());
    }

    // Opens the Meeting Wizard
    public void openMeetingWizard(View view) {

        // creates a Bottom sheet to create a meeting
        CreateMeetingBottomSheetAdapter createMeetingBottomSheetAdapter = new CreateMeetingBottomSheetAdapter();
        meetingAdapter = createMeetingBottomSheetAdapter;
        createMeetingBottomSheetAdapter.show(getSupportFragmentManager() , createMeetingBottomSheetAdapter.getTag());

    }

    public CreateMeetingBottomSheetAdapter getMeetingAdapter(){
        return meetingAdapter;
    }

    // Start the Meeting Creation Wizard
    public void startMeetingWizard(String title, String location){
        Intent intent = new Intent(this, MeetingWizardActivity.class);

        // Give it the title and location
        intent.putExtra("meeting_wizard_title", title);
        intent.putExtra(MeetingWizardActivity.MEETING_LOCATION, location);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}