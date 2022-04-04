package com.ase.konferenzassistent.meetingwizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ase.konferenzassistent.countdown.CountdownActivity;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.shared.InformationBottomSheetAdapter;
import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.checklist.ChecklistItem;
import com.ase.konferenzassistent.checklist.OnAdapterItemClickListener;
import com.ase.konferenzassistent.data.ParticipantData;
import com.ase.konferenzassistent.data.RoomDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeetingWizardActivity extends AppCompatActivity implements OnAdapterItemClickListener, CustomAlertBottomSheetAdapter.onLeaveListener {

    // REMOVE THESE LATER INTO ANOTHER CLASS SMELLY
    public static final String COUNTDOWN_ARRAY = "COUNTDOWN_ARRAY";
    public static final String PARTICIPANT_ARRAY = "PARTICIPANT_ARRAY";
    public static final String MEETING_TITLE = "MEETING_TITLE";
    public static final String MEETING_LOCATION = "MEETING_LOCATION";
    public static final String MEETING_COUNTDOWN = "MEETING_COUNTDOWN";
    public static final String MEETING_CHECKLIST = "MEETING_CHECKLIST";
    // State Constants
    final static int STATE_IS_COUNTDOWN = 0;
    final static int STATE_IS_PARTICIPANT = 1;
    final static int STATE_IS_CHECKLIST = 2;
    static String location;
    // Array List of all Wizard Fragments
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    RoomDB database;
    // Views
    TextView titleText;
    TextView stageText;
    ProgressBar progressBar;
    Button continueButton;
    // Position in the Wizard
    private int wizardPosition = 0;
    // Countdowns deprecated
    private ArrayList<String> mCountdownNames;
    private ArrayList<Long> mCountdownTime;
    private ArrayList<Boolean> mEnabled;
    // advanced Countdown
    private ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObjects;
    // participants
    private ArrayList<Participant> participants;
    // checklists
    private ArrayList<ChecklistItem> checklistItems;

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

    public ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> getAdvancedCountdownObjects() {
        return advancedCountdownObjects;
    }

    public void setAdvancedCountdownObjects(ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObjects) {
        this.advancedCountdownObjects = advancedCountdownObjects;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_wizard);

        // Hide default action Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // MAKE SURE DATA IS LOADED FIRST !!!
        // OR NULL OBJECT
        mCountdownNames = new ArrayList<>();
        mCountdownTime = new ArrayList<>();
        mEnabled = new ArrayList<>();
        advancedCountdownObjects = new ArrayList<>();
        participants = new ArrayList<>();


        // Load Participants Objects
        List<ParticipantData> d;
        database = RoomDB.getInstance(getBaseContext());
        d = database.participantDao().getAll();

        d.forEach(participantData -> {
            Participant participant = new Participant(participantData.getName(), participantData.getEmail(), participantData.getStatus(), false, participantData.getID());
            participants.add(participant);
        });

        setupViews();
        getExtras();

        setupFragments();
        setupListeners();

        // Loads the first Fragment
        loadFragment(0);
    }

    // Setup all needed Fragments at the start
    private void setupFragments() {
        fragmentArrayList.add(new WizardCountdownFragment());
        fragmentArrayList.add(new WizardParticipantFragment());
        fragmentArrayList.add(new WizardChecklistFragment(this, checklistItems));
    }

    // Setup all the listeners on the buttons
    private void setupListeners() {
        // Forwards Button
        continueButton.setOnClickListener(view -> {
            if (wizardPosition < fragmentArrayList.size() - 1) {
                wizardPosition++;
                loadFragment(wizardPosition);
                setForm();
            } else if (wizardPosition == 2) {
                // Check if form is completed
                startCountdownActivity();
            }
        });

        // Backwards Button
        findViewById(R.id.wizard_back_button).setOnClickListener(view -> {
            if (wizardPosition > 0) {
                wizardPosition--;
                loadFragment(wizardPosition);
                setForm();
            }
            // Check if user is already at the first Fragment
            else if (wizardPosition == 0) {
                // Warn user about leaving
                CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
                customAlertBottomSheetAdapter.setWarningText("Solle das Meeting wirklich vorzeitig Beendet werden?");
                customAlertBottomSheetAdapter.setAcceptText("Meeting Verwerfen");
                customAlertBottomSheetAdapter.setDeclineText("Fortfahren");
                customAlertBottomSheetAdapter.show(getSupportFragmentManager(), customAlertBottomSheetAdapter.getTag());
            }
        });

        // Information button
        findViewById(R.id.wizard_information_button).setOnClickListener(view -> {
            // creates a Bottom sheet to display Information
            InformationBottomSheetAdapter informationBottomSheetAdapter = new InformationBottomSheetAdapter();
            // Set the layout
            informationBottomSheetAdapter.setmLayout(R.layout.example_layout);
            informationBottomSheetAdapter.show(getSupportFragmentManager(), informationBottomSheetAdapter.getTag());


        });
    }

    // Links Views
    private void setupViews() {
        titleText = findViewById(R.id.wizard_meeting_title);
        stageText = findViewById(R.id.wizard_stage_title);
        progressBar = findViewById(R.id.progress_wizart_bar);
        progressBar.setProgress(33);
        progressBar.setMax(100);
        continueButton = findViewById(R.id.wizard_continue);
    }

    // Gets the Extras from the intent
    @SuppressWarnings("unchecked")
    private void getExtras() {
        titleText.setText(getIntent().getStringExtra("meeting_wizard_title"));
        location = getIntent().getStringExtra(MEETING_LOCATION);
        advancedCountdownObjects =
                (ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject>)
                        getIntent().getSerializableExtra(MEETING_COUNTDOWN);
        checklistItems = (ArrayList<ChecklistItem>) getIntent().getSerializableExtra(MEETING_CHECKLIST);
    }

    // Loads a Fragment at the specified position
    private void loadFragment(int position) {
        // Gets the fragment from the array
        Fragment fragment = fragmentArrayList.get(position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment, "wizard_fragment" + position);
        transaction.commit();
    }

    // Sets the Form Layout depending on view
    private void setForm() {
        switch (wizardPosition) {
            case STATE_IS_COUNTDOWN:
                stageText.setText("COUNTDOWN EINSTELLEN");
                progressBar.setProgress(33);
                break;
            case STATE_IS_PARTICIPANT:
                stageText.setText("TEILNEHMER HINZUFÜGEN");
                continueButton.setEnabled(true);
                continueButton.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.btn_round));
                continueButton.setTextColor(getColor(R.color.white));
                continueButton.setText("WEITER");
                progressBar.setProgress(66);
                break;
            case STATE_IS_CHECKLIST:
                stageText.setText("CHECKLISTE ABARBEITEN");
                continueButton.setEnabled(false);
                continueButton.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.btn_round_disabled));
                continueButton.setTextColor(getColor(R.color.dark_gray));
                continueButton.setText("MEETING STARTEN");
                progressBar.setProgress(100);
                break;
        }
    }

    public void addNewParticipant(String name, String email, String status) {
        Participant participant = new Participant(name, email, status, true, 0);
        WizardParticipantFragment fragment = (WizardParticipantFragment) fragmentArrayList.get(STATE_IS_PARTICIPANT);

        ParticipantData participantData = new ParticipantData();
        participantData.setName(participant.getName());
        participantData.setEmail(participant.getEmail());
        participantData.setStatus(participant.getStatus());
        participant.setId(participantData.getID());
        participants.add(participant);

        long participantId = database.participantDao().insert(participantData);
        participants.get(participants.size() - 1).setId((int) participantId);

        fragment.onParticipentUpdate();
    }

    public void updateDataSet() {
        WizardParticipantFragment fragment = (WizardParticipantFragment) fragmentArrayList.get(STATE_IS_PARTICIPANT);
        fragment.updateDataSet();
    }

    private void startCountdownActivity() {
        Intent intent = new Intent(this, CountdownActivity.class);

        putExtras(intent);
        // only get Attending Participants

        // start next activity
        startActivity(intent);
        finish();
        //}
    }

    // Sends the data to the Countdown
    private void putExtras(Intent intent) {
        ArrayList<Participant> attendingParticipants = new ArrayList<>();
        participants.forEach(participant -> {
            if (participant.getSelected()) {
                attendingParticipants.add(participant);
            }
        });

        // Put extras
        intent.putExtra(PARTICIPANT_ARRAY, attendingParticipants);
        intent.putExtra(COUNTDOWN_ARRAY, advancedCountdownObjects);
        intent.putExtra(MEETING_LOCATION, location);
        intent.putExtra(MEETING_TITLE, titleText.getText());

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
        WizardChecklistFragment fragment = (WizardChecklistFragment) fragmentArrayList.get(STATE_IS_CHECKLIST);
        int checklistItemsChecked = fragment.checkItem();

        if (checklistItemsChecked == checklistItems.size()) {
            continueButton.setEnabled(true);
            continueButton.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.btn_round));
            continueButton.setTextColor(getColor(R.color.white));

        } else {
            continueButton.setEnabled(false);
            continueButton.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.btn_round_disabled));
            continueButton.setTextColor(getColor(R.color.dark_gray));
        }
    }

}