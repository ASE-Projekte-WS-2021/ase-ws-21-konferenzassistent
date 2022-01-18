package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText luefungstime;
    private EditText lueftungsdauer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find inputs
        luefungstime = findViewById(R.id.lueftungstime);
        lueftungsdauer = findViewById(R.id.lueftungsdauer);
;    }

    public void openChecklistActivity(View view) {
        Intent intent = new Intent(this, ChecklistActivity.class);

        // Default values for Testing purposes
        long maxCountdownTime = 30000;
        long lueftungsCountdownTime = 15000;

        // Get the Lueftungs timer from the EditText
        if(!luefungstime.getText().toString().isEmpty()){
            maxCountdownTime = Long.parseLong(luefungstime.getText().toString()) * 60000;
        }

        // Get th Lueftungsdauer from the EditText
        if(!lueftungsdauer.getText().toString().isEmpty()){
            lueftungsCountdownTime = Long.parseLong(lueftungsdauer.getText().toString()) * 60000;
        }

        // Send them as intent to the next Activity
        intent.putExtra("maxCountdownTime", maxCountdownTime);
        intent.putExtra("maxLueftungsTimer", lueftungsCountdownTime);

        // Start next Activity
        startActivity(intent);
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