package com.example.myapplication;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    // Input fields
    String title;
    String location;

    // should the sheet be leave able
    boolean cancelable = true;
    boolean warning = false;

    // max scroll before it counts as attempt to close
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;
    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style so the background is Transparent
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
                // check if the state is collapsed while its not cancelable
                if(newState == BottomSheetBehavior.STATE_COLLAPSED && !cancelable){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // if its not cancelable open the warning
                if(!cancelable && slideOffset < MIN_SCROLL_FOR_CLOSURE){
                    openWarning();
                }

                // only allows the user to close if its cancelable
                bottomSheetBehavior.setHideable(cancelable);
            }
        });

        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // if cancelable close else show a warning
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

                // Open the Meeting wizard
                if(((MainActivity)getActivity()) != null)
                    ((MainActivity)getActivity()).startMeetingWizard(view);
            }
        });

        // location button clicked
        bi.buttonOrt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Open the Location Sheet
                LocationSelectBottomSheet locationSelectBottomSheet = new LocationSelectBottomSheet();
                locationSelectBottomSheet.show(getParentFragmentManager() , locationSelectBottomSheet.getTag());
            }
        });


        // Add a Text Change Listener to update the Title once text got changed
        bi.textInputMeeting.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // check if string is empty
                if(charSequence.length() != 0){
                    // activate the creation and save the String
                    title = "" + charSequence;
                }
                else{
                    // Change the title back to null
                    title = null;
                }

                // update the create Button
                isCreateable();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Setup the create Button
        isCreateable();
        return bottomSheet;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void openWarning(){
        // check if warning alrady open
        if(!warning){
            // set warning as true
            warning = true;
            // creates a Bottom sheet to create a meeting
            CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter();
            customAlertBottomSheetAdapter.show(getParentFragmentManager() , customAlertBottomSheetAdapter.getTag());
        }
    }

    public void resetWarning(){
        warning = false;
    }

    public void setLocation(String location){
        this.location = location;
        Log.i("TAG", "setLocation: " + location);
        bi.buttonOrt.setText(location);
        bi.buttonOrt.setTextColor(getResources().getColor(R.color.black));
    }

    private void isCreateable(){
        if(title != null){
            bi.dialogCreateButton.setClickable(true);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.red));
            cancelable = false;
        }
        else{
            bi.dialogCreateButton.setClickable(false);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.gray));
            cancelable = true;
        }
    }

    // dismisses the newly created Meeting
    public void dismissMeeting(){
        dismiss();
    }

}
