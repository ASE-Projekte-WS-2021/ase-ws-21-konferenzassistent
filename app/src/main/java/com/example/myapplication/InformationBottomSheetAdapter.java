package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.BottomSheetInformationDisplayBinding;
import com.example.myapplication.databinding.BottomSheetLocationSelectBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class InformationBottomSheetAdapter extends BottomSheetDialogFragment {
    private static final float MIN_SCROLL_FOR_CLOSURE = 0.1f;
    BottomSheetInformationDisplayBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;

    // Location List
    RecyclerViewLocationAdapter recyclerViewLocationAdapter;
    RelativeLayout relativeLayout;

    public void setmLayout(Integer mLayout) {
        this.mLayout = mLayout;
    }

    private Integer mLayout;
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
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_information_display, null);

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
                if(newState == BottomSheetBehavior.STATE_COLLAPSED ){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset < MIN_SCROLL_FOR_CLOSURE){
                    setCancelable(true);
                }
                else
                {
                    setCancelable(false);
                }
            }
        });
        setCancelable(false);

        inflateInformationLayout();

        // cancel button clicked
        bi.informationButtonDismiss.setOnClickListener(viewListener -> dismiss());

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);


        return bottomSheet;
    }

    private void inflateInformationLayout(){
        LayoutInflater layoutInflater = (LayoutInflater)
                this.getLayoutInflater();
        bi.informationLayout.addView(layoutInflater.inflate(mLayout, (ViewGroup) getView(), false), 0);
    }

    // Closes the Sheet
    public void closeLocation(){
        dismiss();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

}
