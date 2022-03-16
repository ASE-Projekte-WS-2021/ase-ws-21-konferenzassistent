package com.example.myapplication;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.CreateMeetingBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class CreateMeetingBottomSheetAdapter extends BottomSheetDialogFragment {

    CreateMeetingBottomSheetBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;

    // should the sheet be leaveable
    boolean cancelable = false;

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.create_meeting_bottom_sheet, null);

        // binding views to data binding
         bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // skip it being collapsable
        bottomSheetBehavior.setSkipCollapsed(true);



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetBehavior.setHideable(false);
        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(cancelable)
                    dismiss();
                else
                    openWarning();
            }
        });

        // edit button clicked
        bi.dialogCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(((MainActivity)getActivity()) != null)
                    ((MainActivity)getActivity()).startMeetingWizard(view);
            }
        });

        return bottomSheet;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void openWarning(){
        // creates a Bottom sheet to create a meeting
        CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter();
        customAlertBottomSheetAdapter.show(getParentFragmentManager() , customAlertBottomSheetAdapter.getTag());


    }

}
