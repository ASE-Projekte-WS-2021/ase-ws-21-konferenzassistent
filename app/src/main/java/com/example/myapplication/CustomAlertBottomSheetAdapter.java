package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.CutsomAlertBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CustomAlertBottomSheetAdapter extends BottomSheetDialogFragment {

    CutsomAlertBottomSheetBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    onLeaveListener listener;
    String warningText = "";
    String acceptText = "";
    String declineText = "";

    public CustomAlertBottomSheetAdapter(onLeaveListener listener) {
        this.listener = listener;
    }

    public void setWarningText(String warningText) {
        this.warningText = warningText;
    }

    public void setAcceptText(String acceptText) {
        this.acceptText = acceptText;
    }

    public void setDeclineText(String declineText) {
        this.declineText = declineText;
    }

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
        View view = View.inflate(bottomSheet.getContext(), R.layout.cutsom_alert_bottom_sheet, null);

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

        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        // cancel buttun clicked
        bi.buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bi.buttonDismissChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLeaving();

                // dismiss the alert
                dismiss();
            }
        });

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        // Set Button Texts
        setupDialogText();

        return bottomSheet;
    }

    // Sets up the Dialog text
    private void setupDialogText() {
        bi.buttonDismiss.setText(declineText);
        bi.buttonDismissChanges.setText(acceptText);
        bi.alertWarningText.setText(warningText);
    }

    @Override
    public void onStart() {
        super.onStart();

        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.clearWarnings();
    }

    public interface onLeaveListener {
        void onLeaving();

        void clearWarnings();
    }
}

