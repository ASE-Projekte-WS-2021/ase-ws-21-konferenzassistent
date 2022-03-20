package com.example.myapplication.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CountdownActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChecklistActivity extends AppCompatActivity implements OnAdapterItemClickListener {
    private long maxCountdownTime;
    private long maxLueftungsTime;

    private long maxAbstandsTime;

    // start Meeting Button
    private Button startMeetingButton;

    private boolean lueftungsSwitchStatus;
    private boolean abstandsSwitchStatus;

    private String location, participantCount;

    private int checkedItems;

    private ArrayList<String> checklistContent = new ArrayList<String>();

    private RecyclerView rvChecklist;
    private List<ChecklistItem> checklistItems;
    private ChecklistAdapter checklistAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_checklist);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle("Pre-Meeting-Checkliste");
            actionBar.setDisplayHomeAsUpEnabled(true); // sets up back button in action bar
        }

        startMeetingButton = findViewById(R.id.startMeetingButton);
        startMeetingButton.setBackgroundResource(R.drawable.btn_disable);
        startMeetingButton.setClickable(false);
        // Get extras
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0);
        maxLueftungsTime = getIntent().getLongExtra("maxLueftungsTimer", 0);

        maxAbstandsTime = getIntent().getLongExtra("maxAbstandsTimer", 0);

        lueftungsSwitchStatus = getIntent().getBooleanExtra("lueftungsSwitchStatus", false);
        abstandsSwitchStatus = getIntent().getBooleanExtra("abstandsSwitchStatus", false);

        location = getIntent().getStringExtra("location");
        participantCount = getIntent().getStringExtra("participantCount");

        rvChecklist = findViewById(R.id.rv_checklist);

                // initialize checklist and recyclerview
        checklistItems = new ArrayList<>();

        checklistContent.add("Desinfektionsmittel bereit");
        checklistContent.add("3G-Regelung o.ä. geprüft");
        checklistContent.add("Masken / Plexiglas geprüft");
        checklistContent.add("Abstände gewährleistet");

        for (int i = 0; i < checklistContent.size(); i++) {
            checklistItems.add(new ChecklistItem(checklistContent.get(i)));
        }

        checklistAdapter = new ChecklistAdapter(this, this, checklistItems);
        rvChecklist.setAdapter(checklistAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        rvChecklist.setLayoutManager(linearLayoutManager);

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

        intent.putExtra("location", location);
        intent.putExtra("participantCount", participantCount);

        // start next activity
        startActivity(intent);
        //}
    }

    //check checklist
    public void checkItem() {
        checkedItems = 0;
        for (ChecklistItem item : checklistItems) {
            if (item.isChecked()) {
                checkedItems++;
            }
        }
        if (checkedItems == checklistItems.size()) { //change startMeetingButton color
            startMeetingButton.setBackgroundResource(R.drawable.btn_default);
            startMeetingButton.setClickable(true);
        } else {
            startMeetingButton.setBackgroundResource(R.drawable.btn_disable);
            startMeetingButton.setClickable(false);
        }
    }

    // adds functionality to back button in action bar
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAdapterItemClick() {
        checkItem();
    }
}