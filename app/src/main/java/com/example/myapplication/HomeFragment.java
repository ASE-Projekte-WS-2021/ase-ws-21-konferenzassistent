package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Home Fragment Class
 *
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    // Meeting database
    private MettingDatabase dbHelper;

    // Components
    private RecyclerView rvMeetings;
    private TextView introText;

    // Meeting List
    private List<Meeting> meetingsList;

    private MeetingHistoryAdapter meetingHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MettingDatabase(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseView();
        createArrayListFromDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Initialises the Components
    private void initialiseView() {
        rvMeetings = getView().findViewById(R.id.rv_history);
        introText = getView().findViewById(R.id.introText);
    }

    // Creates an ArrayList From the Database entries
    private void createArrayListFromDatabase(){
        meetingsList = new ArrayList<>();
        Cursor cursor = dbHelper.readAllData();

        while (cursor.moveToNext()) {
            String mId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String mDate = cursor.getString(cursor.getColumnIndexOrThrow("meeting_date"));
            String mDateEnd = cursor.getString(cursor.getColumnIndexOrThrow("meeting_date_end"));
            String mDuration = cursor.getString(cursor.getColumnIndexOrThrow("meeting_duration"));
            String mLocation = cursor.getString(cursor.getColumnIndexOrThrow("meeting_position"));
            String mNumberParticipants = cursor.getString(cursor.getColumnIndexOrThrow("meeting_number_participants"));
            meetingsList.add(new Meeting(mId,mDate,mDateEnd,mLocation,mDuration,mNumberParticipants));
        }

        /* Log.d("database", Arrays.toString(cursor.getColumnNames()));
        for (Meeting m:  meetingsList) {
            Log.d("database", m.toString());
        }
        */

        meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), meetingsList);
        rvMeetings.setAdapter(meetingHistoryAdapter);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvMeetings.setLayoutManager(linearLayoutManager);

        // meetingHistoryAdapter.notifyDataSetChanged();

        setIntroTextVisibility();
    }

    // Listen for Data Set Changes
    private void onDataChanged() {
        meetingHistoryAdapter.notifyDataSetChanged();
        setIntroTextVisibility();
        linearLayoutManager.scrollToPosition(meetingsList.size() - 1);
    }

    // Sets the Intro Text to invisible once the first meeting got recorded
    private void setIntroTextVisibility() {
        if (meetingsList.isEmpty()) {
            introText.setVisibility(View.VISIBLE);
        } else {
            introText.setVisibility(View.INVISIBLE);
        }
    }
    /*
    public void openPastMeeting(View view){
        Intent intent = new Intent(this, PastMeetingInfoActivity.class);
        startActivity(intent);
    }
     */

}


