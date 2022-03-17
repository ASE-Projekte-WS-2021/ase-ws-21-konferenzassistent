package com.example.myapplication.meetingwizard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.CountdownActivity;
import com.example.myapplication.CustomAlertBottomSheetAdapter;
import com.example.myapplication.R;
import com.example.myapplication.checklist.OnAdapterItemClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MeetingWizardActivity extends AppCompatActivity implements OnAdapterItemClickListener, CustomAlertBottomSheetAdapter.onLeaveListener {

    // Array List of all Wizard Fragments
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    // Position in the Wizard
    private int wizardPosition = 0;

    // State Constants
    final static int STATE_IS_COUNTDOWN = 0;
    final static int STATE_IS_PARTICIPANT = 1;
    final static int STATE_IS_CHECKLIST = 2;

    public ArrayList<String> getmCountdownNames() {
        return mCountdownNames;
    }

    public void setmCountdownNames(ArrayList<String> mCountdownNames) {
        this.mCountdownNames = mCountdownNames;
    }

    public ArrayList<Long> getmCountdownTime() {
        return mCountdownTime;
    }

    public void setmCountdownTime(ArrayList<Long> mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    public ArrayList<Boolean> getmEnabled() {
        return mEnabled;
    }

    public void setmEnabled(ArrayList<Boolean> mEnabled) {
        this.mEnabled = mEnabled;
    }

    // Countdowns
    private ArrayList<String> mCountdownNames;
    private ArrayList<Long> mCountdownTime;
    private ArrayList<Boolean> mEnabled;

    public ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> getAdvancedCountdownObjects() {
        return advancedCountdownObjects;
    }

    public void setAdvancedCountdownObjects(ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObjects) {
        this.advancedCountdownObjects = advancedCountdownObjects;
    }

    // advanced Countdown
    private ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObjects;

    // Views
    TextView titleText;
    TextView stageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_wizard);

        // Hide default action Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        setupFragments();
        setupListeners();

        setupViews();
        getExtras();

        mCountdownNames = new ArrayList<>();
        mCountdownTime = new ArrayList<>();
        mEnabled = new ArrayList<>();
        advancedCountdownObjects = new ArrayList<>();

        // TODO: Give real Countdown Data
        mCountdownNames.add("Lüftungstimer");
        mCountdownTime.add((long)15);
        mEnabled.add(true);

        mCountdownNames.add("Abstandstimer");
        mCountdownTime.add((long)15);
        mEnabled.add(true);

        mCountdownNames.add("Third Timer");
        mCountdownTime.add((long)28);
        mEnabled.add(false);

        mCountdownNames.add("Testtimer");
        mCountdownTime.add((long)55);
        mEnabled.add(false);

        // TODO: LOAD OBJECTS
        RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem child1 =
                new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem(
                        (long) 15,
                        "Fenster sollte geöffnet sein");

        RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem child2 =
                new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem(
                        (long) 15,
                        "Fenster sollte geschlossen sein");

        RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem child3 =
                new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem(
                        (long) 15,
                        "");

        ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> children =
                new ArrayList<>();
        children.add(child1);
        children.add(child2);

        ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> children2 =
                new ArrayList<>();

        children2.add(child3);

        RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject advancedCountdownObject =
                new RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject("Lüftungstimer", true, children);

        RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject advancedCountdownObject2 =
                new RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject("Abstandstimer", false, children2);

        advancedCountdownObjects.add(advancedCountdownObject);
        advancedCountdownObjects.add(advancedCountdownObject2);

        // TODO: END OF DEBUG

        // Loads the first Fragment
        loadFragment(0);
    }

    // Setup all needed Fragments at the start
    private void setupFragments(){
        fragmentArrayList.add(new WizardCountdownFragment());
        fragmentArrayList.add(new WizardParticipantFragment());
        fragmentArrayList.add(new WizardChecklistFragment(this));
    }

    // Setup all the listeners on the buttons
    private void setupListeners(){
        // Forwards Button
        findViewById(R.id.wizard_continue).setOnClickListener(view -> {
            if(wizardPosition < fragmentArrayList.size() - 1){
                wizardPosition ++;
                loadFragment(wizardPosition);
                setForm();
            }
            else if(wizardPosition == 2){
                // Check if form is completed
                startCountdownActivity();
            }
        });

        // Backwards Button
        findViewById(R.id.wizard_back_button).setOnClickListener(view -> {
            if(wizardPosition > 0){
                wizardPosition --;
                loadFragment(wizardPosition);
                setForm();
            }
            // Check if user is already at the first Fragment
            else if(wizardPosition == 0){
                // Warn user about leaving
                CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
                customAlertBottomSheetAdapter.setWarningText("Solle das Meeting wirklich vorzeitig Beendet werden?");
                customAlertBottomSheetAdapter.setAcceptText("Meeting Verwerfen");
                customAlertBottomSheetAdapter.setDeclineText("Fortfahren");
                customAlertBottomSheetAdapter.show(getSupportFragmentManager() , customAlertBottomSheetAdapter.getTag());
            }
        });
    }

    // Links Views
    private void setupViews(){
        titleText = findViewById(R.id.wizard_meeting_title);
        stageText = findViewById(R.id.wizard_stage_title);
    }

    // Gets the Extras from the intent
    private void getExtras(){
        titleText.setText(getIntent().getStringExtra("meeting_wizard_title"));
    }

    // Loads a Fragment at the specified position
    private void loadFragment(int position){
        // Gets the fragment from the array
        Fragment fragment = fragmentArrayList.get(position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment, "wizard_fragment" +position);
        transaction.commit();
    }

    // Sets the Form Layout depending on view
    private void setForm(){
        switch(wizardPosition){
            case STATE_IS_COUNTDOWN:
                stageText.setText("Countdown einstellen");
                break;
            case STATE_IS_PARTICIPANT:
                stageText.setText("Teilnehmer hinzufügen");
                break;
            case STATE_IS_CHECKLIST:
                stageText.setText("Checkliste abarbeiten");
                break;
        }
    }

    // Gets and sets the Countdown Data depending what got entered in the wizard
    public void getCountdownData(){

    }

    private void startCountdownActivity(){
        Intent intent = new Intent(this, CountdownActivity.class);

        intent.putExtra("maxCountdownTime", (long)15);
        intent.putExtra("maxLueftungsTimer",(long) 10);
        intent.putExtra("maxAbstandsTimer", (long)5);

        intent.putExtra("lueftungsSwitchStatus", true);
        intent.putExtra("abstandsSwitchStatus", true);

        intent.putExtra("location", "Regensburg");
        intent.putExtra("participantCount", "24");

        // start next activity
        startActivity(intent);
        //}
    }

    @Override
    public void onLeaving() {
        // go back
        finish();
    }

    @Override
    public void clearWarnings() {
        // Empty
    }

    @Override
    public void onAdapterItemClick() {
        WizardChecklistFragment fragment =(WizardChecklistFragment) fragmentArrayList.get(STATE_IS_CHECKLIST);

        fragment.checkItem();
    }


}