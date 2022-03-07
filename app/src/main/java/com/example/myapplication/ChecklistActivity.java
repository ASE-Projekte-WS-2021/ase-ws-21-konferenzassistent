package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class ChecklistActivity extends AppCompatActivity {
    private long maxCountdownTime;
    private long maxLueftungsTime;

    private long maxAbstandsTime;

    //checklist Checkbox
    private CheckBox cb1, cb2, cb3, cb4;
    //start Meeting Button
    private Button startMeetingButton, disableMeetingButton;

    private boolean lueftungsSwitchStatus;
    private boolean abstandsSwitchStatus;

    private int checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_checklist);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle("Pre-Meeting-Checkliste");
            actionBar.setDisplayHomeAsUpEnabled(true); // sets up back button in action bar
        }

        cb1 = (CheckBox) findViewById(R.id.checkBox); cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3); cb4 = (CheckBox) findViewById(R.id.checkBox4);

        startMeetingButton = findViewById(R.id.startMeetingButton);
        disableMeetingButton = findViewById(R.id.disable_startMeetingButton);
        hideStartMeetingButton();
        // Get extras
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0);
        maxLueftungsTime = getIntent().getLongExtra("maxLueftungsTimer", 0);

        maxAbstandsTime = getIntent().getLongExtra("maxAbstandsTimer", 0);

        lueftungsSwitchStatus = getIntent().getBooleanExtra("lueftungsSwitchStatus", false);
        abstandsSwitchStatus = getIntent().getBooleanExtra("abstandsSwitchStatus", false);

        super.onCreate(savedInstanceState);
    }

    // open the new activity
    public void openCountdownActivity(View view) {
        Intent intent = new Intent(this, CountdownActivity.class);
        //if(checkedItems == 4/*cb1.isChecked() && cb2.isChecked() && cb3.isChecked() && cb4.isChecked()*/){//Meeting will only start if checklist items are selected
          // send to next activity
          intent.putExtra("maxCountdownTime", maxCountdownTime);
          intent.putExtra("maxLueftungsTimer", maxLueftungsTime);
          intent.putExtra("maxAbstandsTimer", maxAbstandsTime);

        intent.putExtra("lueftungsSwitchStatus", lueftungsSwitchStatus);
        intent.putExtra("abstandsSwitchStatus", abstandsSwitchStatus);

          // start next activity
          startActivity(intent);
        //}
    }

    //check checklist
    public void checkItem(View view){
        checkedItems = 0;
        if(cb1.isChecked()){
            checkedItems++;
        }
        if(cb2.isChecked()){
            checkedItems++;
        }
        if(cb3.isChecked()){
            checkedItems++;
        }
        if(cb4.isChecked()){
            checkedItems++;
        }
        if(checkedItems == 4){//change startMeetingButton color
            startMeetingButton.setVisibility(View.VISIBLE);
            disableMeetingButton.setVisibility(View.GONE);
        }else{
            hideStartMeetingButton();
        }
    }

    private void hideStartMeetingButton(){
        startMeetingButton.setVisibility(View.GONE);
        disableMeetingButton.setVisibility(View.VISIBLE);
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