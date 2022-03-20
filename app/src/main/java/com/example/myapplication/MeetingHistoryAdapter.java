package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.RoomDB;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MeetingHistoryViewHolder> implements
CardviewTouchHelperAdapter, CustomAlertBottomSheetAdapter.onLeaveListener{
    private final Context ct;
    private final FragmentManager manager;
    private final List<Meeting> meetingsList;
    private CardviewTouchHelper cTouchHelper;
    private List<String> meeting_id = new ArrayList<>();
    swiped swipedListener;
    private int swipedItemPosition = -1;

    public MeetingHistoryAdapter(Context ct, FragmentManager manager, List<Meeting> meetingsList, swiped swipedListener) {
        this.ct = ct;
        this.manager = manager;
        this.meetingsList = meetingsList;
        this.swipedListener = swipedListener;
    }

    interface swiped{
        void onDeleteSwipe(Integer size);
    }

    @NonNull
    @Override
    public MeetingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.meeting_history_row,parent,false);
        return new MeetingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingHistoryViewHolder holder, int position) {
        //Get Meeting ID
        meeting_id.add(meetingsList.get(position).getId());

        // Get the Values from the meeting List
        String duration = Integer.parseInt(meetingsList.get(position).getDuration())/60 + "";
        String startTime = meetingsList.get(position).getDate().substring(11);
        String endTime = meetingsList.get(position).getDateEnd().substring(11);
        String participants = meetingsList.get(position).getNumberParticipants();
        String ort = meetingsList.get(position).getLocation();
        String date = meetingsList.get(position).getDate().substring(0,10);
        String title = meetingsList.get(position).getTitle();

        // set the Text Values of the Holder
        holder.tvDate.setText(date);
        holder.tvTime.setText(String.format(ct.getString(R.string.meeting_history_minutes_divider),startTime,endTime));
        holder.tvLocation.setText(ort);
        holder.tvDuration.setText(String.format(ct.getString(R.string.meeting_history_minutes_short),duration));
        holder.tvNumParticipants.setText(participants);
        holder.tvTitle.setText(title);

        // set onclick listener on the cardView
        holder.cardView.setOnClickListener(view -> {
            // create a new bottom sheet and set the values for the view
            MeetingBottomSheetAdapter meetingBottomSheetAdapter = new MeetingBottomSheetAdapter();
            meetingBottomSheetAdapter.show(manager , meetingBottomSheetAdapter.getTag());
            meetingBottomSheetAdapter.setValues(
                    String.format(ct.getString(R.string.meeting_history_minutes_long),duration),
                    date,
                    startTime,
                    endTime,
                    participants,
                    ort);

            /*
            Intent intent = new Intent(ct, PastMeetingInfoActivity.class);
            intent.putExtra("Database_ID",Integer.parseInt(meetingsList.get(position).getId()));
            ct.startActivity(intent);
            */
        });
        //set onLONGclick listener on the cardView
    /*    holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // create a new bottom sheet and set the values for the view
                MeetingBottomSheetAdapter meetingBottomSheetAdapter = new MeetingBottomSheetAdapter();
                meetingBottomSheetAdapter.show(manager , meetingBottomSheetAdapter.getTag());
                meetingBottomSheetAdapter.setValues(
                        String.format(ct.getString(R.string.meeting_history_minutes_long),duration),
                        date,
                        startTime,
                        endTime,
                        participants,
                        ort);
                return false;
            }
        });*/
/*
        holder.cardView.setOnTouchListener(new OnSwipeTouchListener(ct) {
            public void onSwipeLeft() {
                Toast.makeText(ct, "left-swipe-test", Toast.LENGTH_LONG).show();
                database.deleteOne(meeting_id);
                holder.cardView.setVisibility(View.GONE);
            }
        });
*/
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.cTouchHelper = cTouchHelper;
    }

    @Override
    public int getItemCount() {
        return meetingsList.size();
    }

    @Override
    public void onItemSwiped(int position) {
        swipedItemPosition = position;

        CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
        customAlertBottomSheetAdapter.setWarningText("Soll dieses Meeting gelöscht werden?");
        customAlertBottomSheetAdapter.setAcceptText("Löschen");
        customAlertBottomSheetAdapter.setDeclineText("Beibehalten");
        customAlertBottomSheetAdapter.show(manager, customAlertBottomSheetAdapter.getTag());
    }

    public static class MeetingHistoryViewHolder extends RecyclerView.ViewHolder implements
    /*View.OnClickListener,*/ View.OnTouchListener, GestureDetector.OnGestureListener {

        CardView cardView;
        TextView tvDate, tvTime, tvLocation, tvDuration, tvNumParticipants, tvTitle;

        GestureDetector cGestureDetector;

        public MeetingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateRow);
            tvTime = itemView.findViewById(R.id.tvTimeRow);
            cardView = itemView.findViewById(R.id.cvRow);
            tvLocation = itemView.findViewById(R.id.tvLocationRow);
            tvDuration = itemView.findViewById(R.id.tvDurationRow);
            tvNumParticipants = itemView.findViewById(R.id.tvParticipantsRow);
            cGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }
/*
        @Override
        public void onClick(View view) {

        }
*/
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            cGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

    @Override
    public void onLeaving() {
        meetingsList.remove(swipedItemPosition);
        RoomDB database = RoomDB.getInstance(ct.getApplicationContext());
        database.meetingDao().delete(database.meetingDao().getOne(Integer.parseInt(meeting_id.get(swipedItemPosition))));
        notifyItemRemoved(swipedItemPosition);

        swipedListener.onDeleteSwipe(meetingsList.size());

        meeting_id.clear();

        swipedItemPosition = -1;
    }

    @Override
    public void clearWarnings() {
        notifyDataSetChanged();
    }
}
