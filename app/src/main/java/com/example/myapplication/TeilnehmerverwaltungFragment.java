package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentMiTeilnehmerverwaltungBinding;

public class TeilnehmerverwaltungFragment extends Fragment {
    FragmentMiTeilnehmerverwaltungBinding bi;

    public TeilnehmerverwaltungFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mi_teilnehmerverwaltung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bi = DataBindingUtil.bind(view);

        // on new participant button
        bi.buttonAddContact.setOnClickListener(viewListener ->{
            ContactCreationBottomSheetAdapter contactCreationBottomSheetAdapter =
                    new ContactCreationBottomSheetAdapter();
            contactCreationBottomSheetAdapter.show(getParentFragmentManager(),
                    contactCreationBottomSheetAdapter.getTag());
        });
    }
}