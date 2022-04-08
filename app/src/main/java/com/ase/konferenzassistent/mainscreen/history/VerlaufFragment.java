package com.ase.konferenzassistent.mainscreen.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.MeetingData;
import com.ase.konferenzassistent.mainscreen.MainScreenActivity;
import com.ase.konferenzassistent.shared.interfaces.OnFilterButtonClickListener;
import com.ase.konferenzassistent.data.MeetingParticipantPair;
import com.ase.konferenzassistent.data.ParticipantData;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.shared.CardviewTouchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Home Fragment Class
 */
public class VerlaufFragment extends Fragment implements OnFilterButtonClickListener, MeetingHistoryAdapter.swiped, MainScreenActivity.FilterButtonListener {

    // Meeting database
    private RoomDB database;
    // Components
    private RecyclerView rvMeetings;
    private TextView introText, isFilteredTextView, pastMeetingCountText;
    // Meeting List
    private List<MeetingData> meetingsList;
    private List<ParticipantData> participantsList;
    private MeetingHistoryAdapter meetingHistoryAdapter;
    // Filter Button
    private boolean dataIsFiltered = false;

    public VerlaufFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());

        ((MainScreenActivity) requireActivity()).setOnFilterButtonClickedListener(VerlaufFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
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
        pastMeetingCountText.setText(Integer.toString(Objects.requireNonNull(rvMeetings.getAdapter()).getItemCount()));
        if (dataIsFiltered) {
            isFilteredTextView.setText(R.string.home_fragment_isFiltered_true);
        } else {
            isFilteredTextView.setText(R.string.home_fragment_isFiltered_false);
        }
    }

    // Initialises the Components
    private void initialiseView() {
        rvMeetings = requireView().findViewById(R.id.rv_history);
        introText = requireView().findViewById(R.id.introText);
        isFilteredTextView = requireView().findViewById(R.id.home_fragment_isFiltered_textView);

        pastMeetingCountText = requireView().findViewById(R.id.fragment_home_past_meet_count);
        // filterButton = getView().findViewById(R.id.main_fragment_filter_button);
    }

    // Creates an ArrayList From the Database entries
    private void createArrayListFromDatabase() {
        meetingsList = new ArrayList<>();
        List<MeetingParticipantPair> d = database.meetingWithParticipantDao().getMeetings();

        d.forEach(meetingParticipantPair -> meetingsList.add(meetingParticipantPair.getMeeting()));

        meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), getParentFragmentManager(), meetingsList, this);
        ItemTouchHelper.Callback callback = new CardviewTouchHelper(meetingHistoryAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvMeetings);

        rvMeetings.setAdapter(meetingHistoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvMeetings.setLayoutManager(linearLayoutManager);

        // meetingHistoryAdapter.notifyDataSetChanged();

        setIntroTextVisibility();
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
        // early return if reset-button instead of filter button clicked
        if (!filterData.isShouldFilter()) {
            if (!dataIsFiltered) {
                return;
            }
            dataIsFiltered = false;
            meetingHistoryAdapter = new MeetingHistoryAdapter(this.getContext(), getParentFragmentManager(), meetingsList, this);
            rvMeetings.setAdapter(meetingHistoryAdapter);
            updatePastMeetingInfo();
            return;
        }
        Stream<MeetingData> filteredMeetingsStream = meetingsList.stream();
        // for every person in peopleList
        // get list of meetings person was in from ConnectionDatabase
        // filter meetings with this person
        List<String> personIds = filterData.getPeopleList().stream()
                .map(person -> Integer.toString(database.participantDao().getIDbyName(person)))
                .collect(Collectors.toList());
        for (String personId : personIds) {
            List<Integer> personMeetingIds = database.meetingWithParticipantDao().getMeetingIDsByParticipantID(personId);
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> personMeetingIds.contains(meeting.getID()));
        }
        // if minCount not -1: filter meetings where numParticipants >= mincount
        if (filterData.getMinCount() != -1) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> database.meetingWithParticipantDao().getMeetingByID(meeting.getID()).getParticipants().size() >= filterData.getMinCount());
        }
        // same respectively for maxCount
        if (filterData.getMaxCount() != -1) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> database.meetingWithParticipantDao().getMeetingByID(meeting.getID()).getParticipants().size() <= filterData.getMaxCount());
        }
        // filter meetings with same location
        if (filterData.getLocation() != null) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting -> meeting.getLocation().equals(filterData.getLocation()));
        }
        // Convert start or end time to millis (probably add method to Meeting class), filter where time >= filterData.dateStart and <= filterData.dateEnd
        if (filterData.getDateStart() != null && filterData.getDateEnd() != null) {
            filteredMeetingsStream = filteredMeetingsStream.filter(meeting ->
                    Long.parseLong(meeting.getStartDate()) >= filterData.getDateStart() &&
                            Long.parseLong(meeting.getEndDate()) <= filterData.getDateEnd()
            );
        }
        // Collect stream as list, sort by id if necessary, change dataIsFiltered, set filtered list to adapter, update view elements
        List<MeetingData> filteredMeetingList = filteredMeetingsStream.collect(Collectors.toList());
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

    public static class FilterData {

        private boolean shouldFilter;
        private List<String> peopleList;
        private int minCount, maxCount;
        private String location;
        private Long dateStart, dateEnd;

        public FilterData(boolean shouldFilter) {
            this.shouldFilter = shouldFilter;
            this.peopleList = null;
            this.dateStart = null;
            this.dateEnd = null;
        }

        public boolean isShouldFilter() {
            return shouldFilter;
        }

        public void setShouldFilter(boolean shouldFilter) {
            this.shouldFilter = shouldFilter;
        }

        public List<String> getPeopleList() {
            return peopleList;
        }

        public void setPeopleList(List<String> peopleList) {
            this.peopleList = peopleList;
        }

        public int getMinCount() {
            return minCount;
        }

        public void setMinCount(int minCount) {
            this.minCount = minCount;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Long getDateStart() {
            return dateStart;
        }

        public void setDateStart(Long dateStart) {
            this.dateStart = dateStart;
        }

        public Long getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Long dateEnd) {
            this.dateEnd = dateEnd;
        }

        @NonNull
        @Override
        public String toString() {
            return "FilterData{" +
                    "shouldFilter=" + shouldFilter +
                    ", peopleList=" + peopleList +
                    ", minCount=" + minCount +
                    ", maxCount=" + maxCount +
                    ", location='" + location + '\'' +
                    ", dateStart=" + dateStart +
                    ", dateEnd=" + dateEnd +
                    '}';
        }
    }
}


