package com.example.myapplication.meetingwizard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentWizardCountdownBinding;
import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WizardCountdownFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WizardCountdownFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentWizardCountdownBinding bi;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WizardCountdownFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WizardCountdownFragment newInstance(String param1, String param2) {
        WizardCountdownFragment fragment = new WizardCountdownFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bi = DataBindingUtil.bind(view);
        assert bi != null;

        // Example how to set Countdown Picker
        bi.wizardNumberPicker.setValue(5);

        bi.countdownSwitch.setOnClickListener(view1 -> {
            if(!bi.countdownSwitch.isChecked()){
                bi.countdownContainer.setVisibility(View.GONE);
            }
            else{
                Log.i("TAG", "onViewCreated: ghua");
                bi.countdownContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wizard_countdown, container, false);
    }
}