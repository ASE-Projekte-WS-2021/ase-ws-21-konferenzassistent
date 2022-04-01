package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.checklist.ChecklistActivity;
import com.example.myapplication.data.RoomDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class LocationParticipantsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean lueftungsSwitchStatus;
    private boolean abstandsSwitchStatus;
    private long maxCountdownTime;
    private long maxLueftungsTime;
    private long maxAbstandsTime;
    private String location = "";
    private String participantCount;

    private Spinner locationsSpinner;
    private List<String> spinnerArrayList;
    private ArrayAdapter<String> spinnerArrayAdapter;

    private ListView participantsView;
    private List<String> participantsArrayList;
    private ArrayAdapter<String> participantsArrayAdapter;

    private RoomDB database;
    private PersonDatabase dbPersonHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_participants);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle("Ort und Teilnehmer");
            actionBar.setDisplayHomeAsUpEnabled(true); // sets up back button in action bar
        }

        database = RoomDB.getInstance(getBaseContext());

        // Get extras
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0);
        maxLueftungsTime = getIntent().getLongExtra("maxLueftungsTimer", 0);

        maxAbstandsTime = getIntent().getLongExtra("maxAbstandsTimer", 0);

        lueftungsSwitchStatus = getIntent().getBooleanExtra("lueftungsSwitchStatus", false);
        abstandsSwitchStatus = getIntent().getBooleanExtra("abstandsSwitchStatus", false);

        locationsSpinner = findViewById(R.id.locations_spinner);


        List<String> locations = database.meetingDao().getLocations();
        spinnerArrayList = new ArrayList<String>(locations);

        spinnerArrayList = new ArrayList<String>();
        spinnerArrayList.add(0, "<leer>");
        spinnerArrayList.add("<Neuen Ort hinzufügen...>");

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        locationsSpinner.setAdapter(spinnerArrayAdapter);
        locationsSpinner.setOnItemSelectedListener(this);

        participantsView = findViewById(R.id.participants_view);

        dbPersonHelper = new PersonDatabase(this);

        /*
        String randomPerson = Integer.toString(new Random().nextInt(100000000) + 1000000);
        dbPersonHelper.addPerson(randomPerson);
        randomPerson = Integer.toString(new Random().nextInt(100000000) + 1000000);
        dbPersonHelper.addPerson(randomPerson);
        */

        participantsArrayList = new ArrayList<String>();

        participantsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, participantsArrayList);
        participantsView.setAdapter(participantsArrayAdapter);
        UIUtils.setListViewHeightBasedOnItems(participantsView);

        participantsView.setOnItemLongClickListener((parent, view, position, id) -> {
            participantsArrayList.remove(position);
            onDataChanged();
            return false;
        });

        onDataChanged();
    }

    public void onAddParticipantButtonClicked(View view) {

        View participantsAlertView = this.getLayoutInflater().inflate(R.layout.participants_alert, null);
        ListView participantsAlertListView = participantsAlertView.findViewById(R.id.participants_alert_listview);
        ArrayAdapter<String> participantsAlertViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dbPersonHelper.getParticipants());
        participantsAlertListView.setAdapter(participantsAlertViewAdapter);
        UIUtils.setListViewHeightBasedOnItems(participantsAlertListView);

        EditText participantsAlertEditText = participantsAlertView.findViewById(R.id.participants_alert_edittext);
        ImageButton participantsAlertAddButton = participantsAlertView.findViewById(R.id.participants_alert_addbutton);

        MaterialAlertDialogBuilder participantInputDialog =
                new MaterialAlertDialogBuilder(LocationParticipantsActivity.this, R.style.dialogAlertStyle)
                        .setTitle("Teilnehmer")
                        .setMessage("Wähle eine*n bestehende*n Teilnehmer*in -oder- Gib einen Namen für den oder die neu anzulegende*n Teilnehmer*in ein.")
                        .setView(participantsAlertView);
        final AlertDialog a = participantInputDialog.create();

        participantsAlertListView.setOnItemClickListener((parent, view1, position, id) -> {
            String value = (String) parent.getItemAtPosition(position);
            participantsArrayList.add(value);
            onDataChanged();
            a.dismiss();
        });

        participantsAlertAddButton.setOnClickListener(view1 -> {
            String value = participantsAlertEditText.getText().toString();
            if (value.length() == 0) {
                Toast.makeText(this, "Bitte einen Namen eingeben.", Toast.LENGTH_SHORT).show();
            } else {
                participantsArrayList.add(value);
                dbPersonHelper.addPerson(value);
                onDataChanged();
                a.dismiss();
            }
        });

        a.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // if last entry selected
        if (spinnerArrayList.size() - 1 == position) {
            locationsSpinner.setSelection(0);

            EditText locationInput = new EditText(LocationParticipantsActivity.this);
            locationInput.setInputType(InputType.TYPE_CLASS_TEXT);
            MaterialAlertDialogBuilder locationInputDialog =
                    new MaterialAlertDialogBuilder(LocationParticipantsActivity.this, R.style.dialogAlertStyle)
                            .setTitle("Ort")
                            .setMessage("Gib einen Namen für den neu anzulegenden Ort ein.")
                            .setView(locationInput)
                            .setPositiveButton("OK", ((dialogInterface, i) -> {
                                String s = locationInput.getText().toString();
                                if (s.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "Bitte einen Ort eingeben.", Toast.LENGTH_SHORT).show();
                                } else {
                                    spinnerArrayList.remove(spinnerArrayList.size() - 1);
                                    spinnerArrayList.add(s);
                                    spinnerArrayList.add("<Neuen Ort hinzufügen...>");

                                    locationsSpinner.setSelection(spinnerArrayList.size() - 2);
                                }
                            }))
                            .setNegativeButton("CANCEL", ((dialogInterface, i) -> {
                                // do nothing
                            }));
            final AlertDialog a = locationInputDialog.create();
            a.show();
        }
    }

    public void openCheckListActivity(View view) {
        Intent intent = new Intent(this, ChecklistActivity.class);

        // Send them as intent to the next Activity
        intent.putExtra("maxCountdownTime", maxCountdownTime);
        intent.putExtra("maxLueftungsTimer", maxLueftungsTime);
        intent.putExtra("maxAbstandsTimer", maxAbstandsTime);

        // Send the switch status to the next Activity
        intent.putExtra("lueftungsSwitchStatus", lueftungsSwitchStatus);
        intent.putExtra("abstandsSwitchStatus", abstandsSwitchStatus);

        String spinnerSelection = locationsSpinner.getSelectedItem().toString();
        if (spinnerSelection == "<leer>") {
            location = "";
        } else {
            location = spinnerSelection;
        }
        intent.putExtra("location", location);

        participantCount = Integer.toString(participantsArrayList.size());
        intent.putExtra("participantCount", participantCount);

        // Start next Activity
        startActivity(intent);
    }

    private void onDataChanged() {
        if (participantsArrayList.size() == 0) {
            findViewById(R.id.tvHintNoParticipants).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tvHintNoParticipants).setVisibility(View.GONE);
        }
        participantsArrayAdapter.notifyDataSetChanged();
        UIUtils.setListViewHeightBasedOnItems(participantsView);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // nicht so tief, Rüdiger!!
    }

    // adds functionality to back button in action bar
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}