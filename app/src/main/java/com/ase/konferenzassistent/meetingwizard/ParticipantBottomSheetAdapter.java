package com.ase.konferenzassistent.meetingwizard;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.BottomSheetParticipantsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ParticipantBottomSheetAdapter extends BottomSheetDialogFragment {
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;
    BottomSheetParticipantsBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    RecyclerViewParticipantListAdapter recycleViewParticipantList;
    // Participant List
    ArrayList<Participant> participants;

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_participants, null);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        //bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // skip it being collapsable
        bottomSheetBehavior.setSkipCollapsed(true);

        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent, null));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                setCancelable(slideOffset < MIN_SCROLL_FOR_CLOSURE);
            }
        });

        setCancelable(false);

        // cancel button clicked
        bi.buttonDismiss.setOnClickListener(viewListener -> dismiss());

        // on new participant button
        bi.participantAddNew.setOnClickListener(viewListener -> {
            ParticipantCreationBottomSheetAdapter participantCreationBottomSheetAdapter = new ParticipantCreationBottomSheetAdapter();
            participantCreationBottomSheetAdapter.show(getParentFragmentManager(), participantCreationBottomSheetAdapter.getTag());
        });

        // Listen for Text input changes to filter locations
        bi.textInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter locations
                recycleViewParticipantList.filter(charSequence.toString());
                if (charSequence == "") {
                    bi.clearTextButton.setVisibility(View.INVISIBLE);
                } else {
                    bi.clearTextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Listener to clear Text
        bi.clearTextButton.setOnClickListener(view1 -> bi.textInputSearch.setText(""));
        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        buildRecyclerView();

        return bottomSheet;
    }

    // Build and fills the recycler view
    private void buildRecyclerView() {
        RecyclerView recyclerView = bi.participantRecycleView;
        recycleViewParticipantList = new RecyclerViewParticipantListAdapter(
                participants,
                this.getContext(),
                true
        );
        recyclerView.setAdapter(recycleViewParticipantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void onParticipentAdded() {
        // get the MeetingWizardActivity
        MeetingWizardActivity activity = ((MeetingWizardActivity) getActivity());

        assert activity != null;
        participants = activity.getParticipants();
        recycleViewParticipantList.updateCopy(participants);
        recycleViewParticipantList.notifyItemRangeInserted(participants.size(), 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recycleViewParticipantList.onDestroy();

        // udate view
        MeetingWizardActivity activity = ((MeetingWizardActivity) getActivity());
        assert activity != null;
        activity.updateDataSet();
    }
}
