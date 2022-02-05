package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Switches to deactivate Timers
    private SwitchMaterial lueftungsSwitch;
    private SwitchMaterial abstandsSwitch;

    private Button startMeetingButton;

    private LinearLayout lueftungstimeClickable;
    private LinearLayout lueftungsdauerClickable;
    private LinearLayout abstandstimeClickable;

    private TextView lueftungstimeClickableText;
    private TextView lueftungsdauerClickableText;
    private TextView abstandstimeClickableText;

    // Countdown timers in minutes
    private long maxCountdownTime = 15;
    private long lueftungsCountdownTime = 5;
    private long abstandsCountdownTime = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Meetingseinstellungen");

        // Find inputs
        lueftungsSwitch = findViewById(R.id.switchLueften);
        abstandsSwitch = findViewById(R.id.switchAbstand);

        lueftungstimeClickable = findViewById(R.id.lueftungstime_clickable);
        lueftungstimeClickableText = findViewById(R.id.lueftungstime_clickable_text);
        setupListener(lueftungstimeClickable,lueftungstimeClickableText);

        lueftungsdauerClickable = findViewById(R.id.lueftungsdauer_clickable);
        lueftungsdauerClickableText = findViewById(R.id.lueftungsdauer_clickable_text);
        setupListener(lueftungsdauerClickable,lueftungsdauerClickableText);

        abstandstimeClickable = findViewById(R.id.abstandstime_clickable);
        abstandstimeClickableText = findViewById(R.id.abstandstime_clickable_text);
        setupListener(abstandstimeClickable,abstandstimeClickableText);

        startMeetingButton = findViewById(R.id.startButton);

;    }

    private void setupListener(LinearLayout clickable, TextView textToChange) {

        EditText minuteInput = new EditText(MainActivity.this);
        minuteInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        MaterialAlertDialogBuilder minuteInputDialog =
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Dauer")
                        .setMessage("Gib die Dauer des Timers in Minuten ein.")
                        .setView(minuteInput)
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            int clickableId = clickable.getId();
                            long parsedLong = Long.parseLong(minuteInput.getText().toString());
                            if (clickableId == lueftungstimeClickable.getId()) {
                                maxCountdownTime = parsedLong;
                            } else if (clickableId == lueftungsdauerClickable.getId()) {
                                lueftungsCountdownTime = parsedLong;
                            } else {
                                abstandsCountdownTime = parsedLong;
                            }
                            textToChange.setText(maxCountdownTime + " Minuten");
                        })
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                            // do nothing
                        });
        final AlertDialog a = minuteInputDialog.create();
        clickable.setOnClickListener(view -> a.show());
    }

    public void openChecklistActivity(View view) {
        Intent intent = new Intent(this, ChecklistActivity.class);

        // Send them as intent to the next Activity
        intent.putExtra("maxCountdownTime", maxCountdownTime);
        intent.putExtra("maxLueftungsTimer", lueftungsCountdownTime);
        intent.putExtra("maxAbstandsTimer", abstandsCountdownTime);

        // Send the switch status to the next Activity
        intent.putExtra("lueftungsSwitchStatus", lueftungsSwitch.isChecked());
        intent.putExtra("abstandsSwitchStatus", abstandsSwitch.isChecked());

        // Start next Activity
        startActivity(intent);
    }

    public void checkRadio(View view) {
        int checkedItems = 0;
        if(lueftungsSwitch.isChecked()){
            checkedItems++;
        }
        if(abstandsSwitch.isChecked()){
            checkedItems++;
        }
        if(checkedItems != 0){//change startMeetingButton color
            startMeetingButton.setEnabled(true);
            startMeetingButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            startMeetingButton.setTextColor(getResources().getColor(R.color.white));
        }else{
            startMeetingButton.setEnabled(false);
            startMeetingButton.setBackgroundColor(getResources().getColor(R.color.gray));
            startMeetingButton.setTextColor(getResources().getColor(R.color.dark_gray));
        }
    }
}

/*
Starting another activity:
Take note of the details in this method. They're required for the system to recognize the method as compatible with the android:onClick attribute. Specifically, the method has the following characteristics:
    Public access.
    A void or, in Kotlin, an implicit unit return value.
    A View as the only parameter. This is the View object you clicked at the end of Step 1.

The Intent constructor takes two parameters, a Context and a Class.
    The Context parameter is used first because the Activity class is a subclass of Context.
    The Class parameter of the app component, to which the system delivers the Intent, is, in this case, the activity to start.
The putExtra() method adds the value of EditText to the intent. An Intent can carry data types as key-value pairs called extras.
    Your key is a public constant EXTRA_MESSAGE because the next activity uses the key to retrieve the text value. It's a good practice to define keys for intent extras with your app's package name as a prefix. This ensures that the keys are unique, in case your app interacts with other apps.

The startActivity() method starts an instance of the ChecklistActivity that's specified by the Intent.
*/