package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.meetingwizard.ParticipantCreationBottomSheetAdapter;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding bi;


    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // on new participant button
        bi.buttonAddContact.setOnClickListener(viewListener ->{
            ContactCreationBottomSheetAdapter contactCreationBottomSheetAdapter = new ContactCreationBottomSheetAdapter();
            contactCreationBottomSheetAdapter.show(getParentFragmentManager(), contactCreationBottomSheetAdapter.getTag());
        });
    }
}