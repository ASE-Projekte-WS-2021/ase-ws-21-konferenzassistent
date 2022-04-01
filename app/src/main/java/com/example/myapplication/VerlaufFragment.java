package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.MeetingParticipantPair;
import com.example.myapplication.data.ParticipantData;
import com.example.myapplication.data.RoomDB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Home Fragment Class
 */
public class VerlaufFragment extends Fragment implements OnFilterButtonClickListener, MeetingHistoryAdapter.swiped, MainActivity.FilterButtonListener {

    ViewDataBinding bi;
    // Meeting database
    private RoomDB database;
    // Components
    private RecyclerView rvMeetings;
    private TextView introText, isFilteredTextView, pastMeetingCountText;
    // Meeting List
    private List<Meeting> meetingsList;
    private List<ParticipantData> participantsList;
    private MeetingHistoryAdapter meetingHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    // Filter Button
    private Button filterButton;
    private boolean dataIsFiltered = false;

    public VerlaufFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());

        ((MainActivity) getActivity()).setOnFilterButtonClickedListener(VerlaufFragment.this);
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

        participantsList = database.participantDao().getAll();
        createArrayListFromDatabase();
        updatePastMeetingInfo();
    }

    private void updatePastMeetingInfo() {
        // update past meeting counter
        pastMeetingCountText.setText(Integer.toString(rvMeetings.getAdapter().getItemCount()));
        if (dataIsFiltered) {
            isFilteredTextView.setText(R.string.home_fragment_isFiltered_true);
        } else {
            isFilteredTextView.setText(R.string.home_fragment_isFiltered_false);
        }
    }

    // Initialises the Components
    private void initialiseView() {
        rvMeetings = getView().findViewById(R.id.rv_history);
        introText = getView().findViewById(R.id.introText);
        isFilteredTextView = getView().findViewById(R.id.home_fragment_isFiltered_textView);

        pastMeetingCountText = getView().findViewById(R.id.fragment_home_past_meet_count);
        // filterButton = getView().findViewById(R.id.main_fragment_filter_button);
    }

    private void setupButtonListener() {
        /*
        filterButton.setOnClickListener(view -> {

            final MeetingFilterBottomSheet meetingFilterBottomSheet = new MeetingFilterBottomSheet(meetingsList, participantsList, this);
            meetingFilterBottomSheet.show(getParentFragmentManager(), meetingFilterBottomSheet.getTag());

        });
        */

    }

    // Creates an ArrayList From the Database entries
    private void createArrayListFromDatabase() {
        meetingsList = new ArrayList<>();
        // List<Participant> participants = new ArrayList<>(); // never used

        List<MeetingParticipantPair> d = database.meetingWithParticipantDao().getMeetings();

        d.forEach(data -> {

            meetingsList.add(new Meeting(
                    "" + data.getMeeting().getID(),
                    data.getMeeting().getStartDate(),
                    data.getMeeting().getEndDate(),
                    data.getMeeting().getLocation(),
                    data.getMeeting().getTitle(),
                    "" + data.getMeeting().getDuration(),
                    "" + data.getParticipants().size()));
        });


        meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), getParentFragmentManager(), meetingsList, this);
        ItemTouchHelper.Callback callback = new CardviewTouchHelper(meetingHistoryAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        meetingHistoryAdapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvMeetings);

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

        // early return if reset-button instead of filter button clicked
        if (!filterData.isShouldFilter()) {
            if (!dataIsFiltered) {
                return;
            }
            dataIsFiltered = false;
            // TODO duplicate code from createArrayFromDatabase
            meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), getParentFragmentManager(), meetingsList, this);
            rvMeetings.setAdapter(meetingHistoryAdapter);
            updatePastMeetingInfo();
            return;
        }
        Stream<Meeting> filteredMeetingsStream = meetingsList.stream();
        // for every person in peopleList
        // get list of meetings person was in from ConnectionDatabase
        // filter meetings with this person
        List<String> personIds = filterData.getPeopleList().stream()
                .map(person -> Integer.toString(database.participantDao().getIDbyName(person)))
                .collect(Collectors.toList());
        for (String personId : personIds) {
            List<Integer> personMeetingIds = database.meetingWithParticipantDao().getMeetingIDsByParticipantID(personId);
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> personMeetingIds.contains(Integer.parseInt(meeting.getId())));
        }
        // if minCount not -1: filter meetings where numParticipants >= mincount
        if (filterData.getMinCount() != -1) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> Integer.parseInt(meeting.getNumberParticipants()) >= filterData.getMinCount());
        }
        // same respectively for maxCount
        if (filterData.getMaxCount() != -1) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> Integer.parseInt(meeting.getNumberParticipants()) <= filterData.getMaxCount());
        }
        // filter meetings with same location
        if (filterData.getLocation() != null) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> meeting.getLocation().equals(filterData.getLocation()));
        }
        // Convert start or end time to millis (probably add method to Meeting class), filter where time >= filterData.dateStart and <= filterData.dateEnd
        if (filterData.getDateStart() != null && filterData.getDateEnd() != null) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting ->
                    meeting.getLongDateStart() >= filterData.getDateStart() &&
                            meeting.getLongDateEnd() <= filterData.getDateEnd()
            );
        }
        // Collect stream as list, sort by id if necessary, change dataIsFiltered, set filtered list to adapter, update view elements
        List<Meeting> filteredMeetingList = filteredMeetingsStream.collect(Collectors.toList());
        dataIsFiltered = filteredMeetingList.size() != meetingsList.size();
        rvMeetings.setAdapter(new MeetingHistoryAdapter(
                this.getContext(),
                getParentFragmentManager(),
                filteredMeetingList,
                this)
        );
        updatePastMeetingInfo();

    }

    @Override
    public void onDeleteSwipe(Integer size) {
        if (size < 1) {
            introText.setVisibility(View.VISIBLE);

        }

        updatePastMeetingInfo();
    }

    @Override
    public void onFilterButtonClicked() {
        final MeetingFilterBottomSheet meetingFilterBottomSheet = new MeetingFilterBottomSheet(meetingsList, participantsList, this);
        meetingFilterBottomSheet.show(getParentFragmentManager(), meetingFilterBottomSheet.getTag());
    }
    /*
    public void openPastMeeting(View view){
        Intent intent = new Intent(this, PastMeetingInfoActivity.class);
        startActivity(intent);
    }
     */
}


