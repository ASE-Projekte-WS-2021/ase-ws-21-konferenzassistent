package com.ase.konferenzassistent.mainscreen.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.BottomSheetCreateItemBinding;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class EditItemBottomSheet extends BottomSheetDialogFragment {
    BottomSheetCreateItemBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    timerEdit listener;
    Integer position;
    RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem mItem;

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
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_create_item, null);

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
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(viewListener -> dismiss());

        bi.dialogCreateButton.setOnClickListener(viewListener ->
        {
            mItem.setSubCountdown((long) bi.itemNumberPicker.getValue());
            mItem.setSubCountdownDescription(bi.itemDescription.getText().toString());
            listener.onEditFinish(mItem, position);
        });

        bi.itemNumberPicker.setValue(mItem.getSubCountdown().intValue());
        bi.itemDescription.setText(mItem.getSubCountdownDescription());

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        return bottomSheet;
    }

    // Initializes the Presets
    public void initTimer(RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem mItem, timerEdit listener, int position) {
        this.mItem = mItem;
        this.listener = listener;
        this.position = position;
    }

    // Closes the Sheet
    public void closeCreation() {
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public interface timerEdit {
        void onEditFinish(RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem mItem, int position);
    }

}
