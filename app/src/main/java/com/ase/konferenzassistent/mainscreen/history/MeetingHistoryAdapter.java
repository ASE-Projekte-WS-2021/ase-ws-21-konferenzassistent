package com.ase.konferenzassistent.mainscreen.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.shared.Interfaces.CardviewTouchHelperAdapter;

import java.util.List;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MeetingHistoryViewHolder> implements
        CardviewTouchHelperAdapter, CustomAlertBottomSheetAdapter.onLeaveListener {
    private final Context ct;
    private final FragmentManager manager;
    private final List<Meeting> meetingsList;
    final swiped swipedListener;
    private int swipedItemPosition = -1;

    public MeetingHistoryAdapter(Context ct, FragmentManager manager, List<Meeting> meetingsList, swiped swipedListener) {
        this.ct = ct;
        this.manager = manager;
        this.meetingsList = meetingsList;
        this.swipedListener = swipedListener;
    }

    @NonNull
    @Override
    public MeetingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.meeting_history_row, parent, false);
        return new MeetingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingHistoryViewHolder holder, int position) {

        // Get the Values from the meeting List
        int id = Integer.parseInt(meetingsList.get(position).getId());
        String duration = Integer.parseInt(meetingsList.get(position).getDuration()) / 60 + "";
        String startTime = meetingsList.get(position).getDate().substring(11);
        String endTime = meetingsList.get(position).getDateEnd().substring(11);
        String participants = meetingsList.get(position).getNumberParticipants();
        String ort = meetingsList.get(position).getLocation();
        String date = meetingsList.get(position).getDate().substring(0, 10);
        String title = meetingsList.get(position).getTitle();

        // set the Text Values of the Holder
        holder.tvDate.setText(date);
        holder.tvTime.setText(String.format(ct.getString(R.string.meeting_history_minutes_divider), startTime, endTime));
        holder.tvLocation.setText(ort);
        holder.tvDuration.setText(String.format(ct.getString(R.string.meeting_history_minutes_short), duration));
        holder.tvNumParticipants.setText(participants);
        holder.tvTitle.setText(title);

        // set onclick listener on the cardView
        holder.cardView.setOnClickListener(view -> {
            // create a new bottom sheet and set the values for the view
            MeetingBottomSheetAdapter meetingBottomSheetAdapter = new MeetingBottomSheetAdapter();
            meetingBottomSheetAdapter.show(manager, meetingBottomSheetAdapter.getTag());
            meetingBottomSheetAdapter.setValues(
                    id,
                    String.format(ct.getString(R.string.meeting_history_minutes_long), duration),
                    date,
                    startTime,
                    endTime,
                    participants,
                    ort);
        });
    }

    @Override
    public int getItemCount() {
        return meetingsList.size();
    }

    @Override
    public void onItemSwiped(int position) {
        swipedItemPosition = position;

        CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
        customAlertBottomSheetAdapter.setWarningText(ct.getString(R.string.meeting_delete_warning));
        customAlertBottomSheetAdapter.setAcceptText(ct.getString(R.string.meeting_delete_warning_yes));
        customAlertBottomSheetAdapter.setDeclineText(ct.getString(R.string.meeting_delete_warning_no));
        customAlertBottomSheetAdapter.show(manager, customAlertBottomSheetAdapter.getTag());
    }

    @Override
    public void onLeaving() {
        RoomDB database = RoomDB.getInstance(ct.getApplicationContext());
        database.meetingDao().delete(database.meetingDao().getOne(Integer.parseInt(meetingsList.get(swipedItemPosition).getId())));

        notifyItemRemoved(swipedItemPosition);
        meetingsList.remove(swipedItemPosition);
        swipedListener.onDeleteSwipe(meetingsList.size());

        swipedItemPosition = -1;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void clearWarnings() {
        notifyDataSetChanged();
    }

    public interface swiped {
        void onDeleteSwipe(Integer size);
    }

    public static class MeetingHistoryViewHolder extends RecyclerView.ViewHolder implements
            View.OnTouchListener, GestureDetector.OnGestureListener {

        final CardView cardView;
        final TextView tvDate;
        final TextView tvTime;
        final TextView tvLocation;
        final TextView tvDuration;
        final TextView tvNumParticipants;
        final TextView tvTitle;

        final GestureDetector cGestureDetector;

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

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            cGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }
}
