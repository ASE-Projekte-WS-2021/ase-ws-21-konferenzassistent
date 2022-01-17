package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class ChecklistActivity extends AppCompatActivity {

    //checklist Checkbox
    private CheckBox cbTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        cbTest = (CheckBox) findViewById(R.id.checkBox);
    }

    // open the new activity
    public void openCountdownActivity(View view) {
        Intent intent = new Intent(this, CountdownActivity.class);
        if(cbTest.isChecked()){//Meeting will only start if checklist item is selected
            startActivity(intent);
        }
    }
}