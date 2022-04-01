package com.ase.konferenzassistent.mainscreen.meetingcreation;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.BottomSheetLocationSelectBinding;
import com.ase.konferenzassistent.mainscreen.MainScreenActivity;
import com.ase.konferenzassistent.mainscreen.recycleviews.RecyclerViewLocationAdapter;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class LocationSelectBottomSheet extends BottomSheetDialogFragment {
    BottomSheetLocationSelectBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    RecyclerViewLocationAdapter recyclerViewLocationAdapter;
    // Location List
    private ArrayList<String> locationNames;

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
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_location_select, null);

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
        bi.buttonDismiss.setOnClickListener(viewListener -> dismiss());

        // Add a setOnEditorActionListener to close the Sheet once the user is done typing
        // https://stackoverflow.com/a/5100007
        bi.textInputSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            switch (i) {
                case EditorInfo.IME_ACTION_DONE:
                case EditorInfo.IME_ACTION_NEXT:
                case EditorInfo.IME_ACTION_PREVIOUS:
                    // the user is done typing.
                    if (!bi.textInputSearch.getText().toString().equals("")) {

                        // Send Location to Meeting Adapter
                        if (((MainScreenActivity) getActivity()) != null)
                            ((MainScreenActivity) getActivity()).getMeetingAdapter()
                                    .setLocation(bi.textInputSearch.getText().toString());

                        // Dismiss the Sheet
                        dismiss();
                    }
                    return true;
            }

            return false; // pass on to other listeners.
        });

        // Listen for Text input changes to filter locations
        bi.textInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter locations
                recyclerViewLocationAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        buildRecyclerView();

        return bottomSheet;
    }

    // Build and fills the recycler view
    private void buildRecyclerView() {
        RecyclerView recyclerView = bi.locationRecyclerView;
        recyclerViewLocationAdapter = new RecyclerViewLocationAdapter(
                locationNames,
                this.getContext()
        );
        recyclerView.setAdapter(recyclerViewLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    // Initializes the Presets
    public void initLocation(ArrayList<String> locationNames) {
        this.locationNames = locationNames;
    }

    // Closes the Sheet
    public void closeLocation() {
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}
