package com.ase.konferenzassistent.meetingwizard;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.BottomSheetParticipantsAddNewBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ParticipantCreationBottomSheetAdapter extends BottomSheetDialogFragment implements CustomAlertBottomSheetAdapter.onLeaveListener, RecycleViewContactList.contactListener {
    // max scroll before it counts as attempt to close
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;
    BottomSheetParticipantsAddNewBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    ParticipantImportContactBottomSheetAdapter participantImportContactBottomSheetAdapter;
    // should the sheet be leave able
    boolean cancelable = true;
    boolean warning = false;

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
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_participants_add_new, null);

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
                // check if the state is collapsed while its not cancelable
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && !cancelable) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // if its not cancelable open the warning
                if (!cancelable && slideOffset < MIN_SCROLL_FOR_CLOSURE) {
                    openWarning();
                }

                // only allows the user to close if its cancelable
                bottomSheetBehavior.setHideable(cancelable);
            }

        });

        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(viewListener -> {
            // if cancelable close else show a warning
            if (cancelable)
                dismiss();
            else
                openWarning();
        });

        // edit button clicked
        bi.dialogCreateButton.setOnClickListener(viewListener -> {
            String pName = bi.participantInputName.getText().toString();
            String pEmail = bi.participantInputEmail.getText().toString();
            String pStatus = bi.participantInputStatus.getText().toString();
            // check for valid email
            if (!pEmail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(pEmail).matches()) {
                Toast.makeText(getContext(), "Bitte eine gültige Email-Adresse eingeben.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new Entry
            MeetingWizardActivity activity = ((MeetingWizardActivity) getActivity());

            assert activity != null;
            activity.addNewParticipant(
                    pName,
                    pEmail,
                    pStatus);
            dismiss();
        });

        // open Contacts
        bi.buttonImportParticipant.setOnClickListener(viewListener -> {
            participantImportContactBottomSheetAdapter =
                    new ParticipantImportContactBottomSheetAdapter();

            participantImportContactBottomSheetAdapter.setListener(this);
            participantImportContactBottomSheetAdapter.show(getParentFragmentManager(),
                    participantImportContactBottomSheetAdapter.getTag());
        });

        // Add a Text Change Listener to update the Title once text got changed
        bi.participantInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // check if string is empty
                // update the create Button
                // update the create Button
                isCreateable(charSequence.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // cancel button clicked
        //bi.buttonDismiss.setOnClickListener(viewListener -> dismiss());

        isCreateable(false);
        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        return bottomSheet;
    }

    // Enables button if title got set
    private void isCreateable(@NonNull Boolean createAble) {
        // if title is set enable button and set color to red
        if (createAble) {
            bi.dialogCreateButton.setClickable(true);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.corona_blue, null));
            cancelable = false;
        }
        // if title is not set disable button and set color to gray
        else {
            bi.dialogCreateButton.setClickable(false);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.gray, null));
            cancelable = true;
        }
    }

    // Closes the Sheet
    public void closeLocation() {
        dismiss();
    }

    // open the warning dialog
    private void openWarning() {
        // check if warning alrady open
        if (!warning) {
            // set warning as true
            warning = true;
            // creates a Bottom sheet to create a meeting
            CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
            customAlertBottomSheetAdapter.setWarningText("Soll dieser neue Teilnehmer verworfen werden?");
            customAlertBottomSheetAdapter.setAcceptText("Änderungen Verwerfen");
            customAlertBottomSheetAdapter.setDeclineText("Weiter Bearbeiten");
            customAlertBottomSheetAdapter.show(getParentFragmentManager(), customAlertBottomSheetAdapter.getTag());
        }
    }

    // resets the warning dialog so it can get opened again
    public void resetWarning() {
        warning = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    // Dismisses the newly created Meeting
    public void dismissCreation() {
        dismiss();
    }

    @Override
    public void onLeaving() {
        dismissCreation();
    }

    @Override
    public void clearWarnings() {
        resetWarning();
    }

    // Enter name into Name field
    @Override
    public void onContactSelected(String name) {
        bi.participantInputName.setText(name);
        participantImportContactBottomSheetAdapter.dismiss();
    }

}
