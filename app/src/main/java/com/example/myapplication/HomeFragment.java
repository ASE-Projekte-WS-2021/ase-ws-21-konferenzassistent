package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.data.MeetingData;
import com.example.myapplication.data.ParticipantData;
import com.example.myapplication.data.RoomDB;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Home Fragment Class
 *
 */
public class HomeFragment extends Fragment  implements OnFilterButtonClickListener {

    public HomeFragment() {
        // Required empty public constructor
    }

    ViewDataBinding bi;

    // Meeting database
    private MettingDatabase dbHelper;

    // Components
    private RecyclerView rvMeetings;
    private TextView introText, pastMeetingCountText;

    // Meeting List
    private List<Meeting> meetingsList;

    private MeetingHistoryAdapter meetingHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    // Filter Button
    private Button filterButton;

    private boolean dataIsFiltered = false;


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
        setupButtonListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        createArrayListFromDatabase();
        updateRKIandPastMeetingInfo();
    }

    private void updateRKIandPastMeetingInfo() {
        // update RKI info
        // todo

        // update past meeting counter
        pastMeetingCountText.setText(Integer.toString(meetingsList.size()));
    }

    // Initialises the Components
    private void initialiseView() {
        rvMeetings = getView().findViewById(R.id.rv_history);
        introText = getView().findViewById(R.id.introText);

        pastMeetingCountText = getView().findViewById(R.id.fragment_home_past_meet_count);
        filterButton = getView().findViewById(R.id.main_fragment_filter_button);
    }

    private void setupButtonListener() {
        filterButton.setOnClickListener(view -> {

            final MeetingFilterBottomSheet meetingFilterBottomSheet = new MeetingFilterBottomSheet(meetingsList,this);
            meetingFilterBottomSheet.show(getParentFragmentManager(),meetingFilterBottomSheet.getTag());

        });
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
            String mNTitle = cursor.getString(cursor.getColumnIndexOrThrow("meeting_title"));
            meetingsList.add(new Meeting(mId,mDate,mDateEnd,mLocation,mNTitle, mDuration, mNumberParticipants));
        }



        List<ParticipantData> d = new ArrayList<>();
        RoomDB database = RoomDB.getInstance(getContext());
        ParticipantData participantData = new ParticipantData();
        participantData.setName("Baum");
        participantData.setStatus("Schule");
        database.participantDao().insert(participantData);
        d = database.participantDao().getAll();

        Log.i("TAG", "createArrayListFromDatabase: " + d.get(0).getStatus());;
        /* Log.d("database", Arrays.toString(cursor.getColumnNames()));
        for (Meeting m:  meetingsList) {
            Log.d("database", m.toString());
        }
        */

        meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), getParentFragmentManager(), meetingsList);
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

    @Override
    public void onFilterButtonClicked(FilterData filterData) {
        // TODO filter meetingsList based on filterData
        Log.d("test",filterData.toString());

        /*
        // early return if reset-button instead of filter button clicked
        if (!filterData.isShouldFilter()) {
            if (!dataIsFiltered) {
                return;
            }
            // TODO duplicate code from createArrayFromDatabase
            meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), getParentFragmentManager(), meetingsList);
            rvMeetings.setAdapter(meetingHistoryAdapter);
            dataIsFiltered = false;
            return;
        }
        Stream<Meeting> filteredMeetingsStream = meetingsList.stream();
        */
        // for every person in peopleList
        // get list of meetings person was in from ConnectionDatabase
        // filter meetings with this person
        /*
        filteredMeetingsStream.filter(meeting -> personMeetingIds.contains(meeting.getId()));
        */
        // if minCount not -1: filter meetings where numParticipants >= mincount
        /*
        if (filterData.getMinCount() != -1) {
            filteredMeetingsStream.filter(meeting -> Integer.parseInt(meeting.getNumberParticipants()) >= filterData.getMinCount());
        }
        */
        // same respectively for maxCount
        /*
        if (filterData.getMaxCount() != -1) {
            filteredMeetingsStream.filter(meeting -> Integer.parseInt(meeting.getNumberParticipants()) <= filterData.getMaxCount());
        }
        */
        // filter meetings with same location
        /*
        if (!filterData.getLocation().equals("null")) {
            filteredMeetingsStream.filter(meeting -> meeting.getLocation() == filterData.getLocation());
        }
        */
        // Convert start or end time to millis (probably add method to Meeting class), filter where time >= filterData.dateStart and <= filterData.dateEnd
        /*
        if (!filterData.getDateStart() == null || !filterData.getDateEnd() == null) {
            filteredMeetingsStream.filter(meeting -> meeting.asMilli() >= filterData.getDateStart() && meeting.asMilli() <= filterData.getDateEnd()
        }
        */
        // Collect stream as list, sort by id if necessary, change dataIsFiltered, set filtered list to adapter
        /*
        List<Meeting> filteredMeetingList = filteredMeetingsStream.collect(Collectors.toList());
        dataIsFiltered = true;
        rvMeetings.setAdapter(new MeetingHistoryAdapter(
                this.getContext(),
                getParentFragmentManager(),
                filteredMeetingList)
        );
        */
    }
    /*
    public void openPastMeeting(View view){
        Intent intent = new Intent(this, PastMeetingInfoActivity.class);
        startActivity(intent);
    }
     */
}


