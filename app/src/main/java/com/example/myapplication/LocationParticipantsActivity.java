package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class LocationParticipantsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner locationsSpinner;
    private List<String> spinnerArray;
    private ArrayAdapter<String> spinnerArrayAdapter;

    private ListView participantsView;
    private List<String> participantsArray;
    private ArrayAdapter<String> participantsArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_participants);

        locationsSpinner = findViewById(R.id.locations_spinner);

        spinnerArray = new ArrayList<String>();
        spinnerArray.add("<leer>");
        spinnerArray.add("<Neuen Ort hinzufügen...>");

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        locationsSpinner.setAdapter(spinnerArrayAdapter);
        locationsSpinner.setOnItemSelectedListener(this);

        participantsView = findViewById(R.id.participants_view);

        participantsArray = new ArrayList<String>();
        participantsArray.add("Max Mustermann");
        participantsArray.add("TEST test");
        participantsArray.add("Definitv kein Roboter");

        participantsArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,participantsArray);
        participantsView.setAdapter(participantsArrayAdapter);
        UIUtils.setListViewHeightBasedOnItems(participantsView);

        participantsView.setOnItemLongClickListener((parent, view, position, id) -> {
            participantsArray.remove(position);
            participantsArrayAdapter.notifyDataSetChanged();
            UIUtils.setListViewHeightBasedOnItems(participantsView);
            return false;
        });
    }

    public void onAddParticipantButtonClicked(View view) {
        EditText participantInput = new EditText(LocationParticipantsActivity.this);
        participantInput.setInputType(InputType.TYPE_CLASS_TEXT);
        MaterialAlertDialogBuilder participantInputDialog =
                new MaterialAlertDialogBuilder(LocationParticipantsActivity.this)
                        .setTitle("Teilnehmer")
                        .setMessage("Gib einen Namen für den neu anzulegenden Teilnehmer ein.")
                        .setView(participantInput)
                        .setPositiveButton("OK",((dialogInterface, i) -> {
                            String s = participantInput.getText().toString();
                            if (s.length() == 0) {
                                Toast.makeText(getApplicationContext(),"Bitte einen Namen eingeben.", Toast.LENGTH_SHORT).show();
                            } else {
                                participantsArray.add(s);
                                participantsArrayAdapter.notifyDataSetChanged();
                                UIUtils.setListViewHeightBasedOnItems(participantsView);
                            }
                        }))
                        .setNegativeButton("CANCEL",((dialogInterface, i) -> {
                            // do nothing
                        }));
        final AlertDialog a = participantInputDialog.create();
        a.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // if last entry selected
        if (spinnerArray.size() - 1 == position) {
            locationsSpinner.setSelection(0);

            EditText locationInput = new EditText(LocationParticipantsActivity.this);
            locationInput.setInputType(InputType.TYPE_CLASS_TEXT);
            MaterialAlertDialogBuilder locationInputDialog =
                    new MaterialAlertDialogBuilder(LocationParticipantsActivity.this)
                    .setTitle("Ort")
                    .setMessage("Gib einen Namen für den neu anzulegenden Ort ein.")
                    .setView(locationInput)
                    .setPositiveButton("OK",((dialogInterface, i) -> {
                        String s = locationInput.getText().toString();
                        if (s.length() == 0) {
                            Toast.makeText(getApplicationContext(),"Bitte einen Ort eingeben.", Toast.LENGTH_SHORT).show();
                        } else {
                            spinnerArray.remove(spinnerArray.size() - 1);
                            spinnerArray.add(s);
                            spinnerArray.add("<Neuen Ort hinzufügen...>");

                            locationsSpinner.setSelection(spinnerArray.size() - 2);
                        }
                    }))
                    .setNegativeButton("CANCEL",((dialogInterface, i) -> {
                        // do nothing
                    }));
            final AlertDialog a = locationInputDialog.create();
            a.show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // nicht so tief, Rüdiger!!
    }
}