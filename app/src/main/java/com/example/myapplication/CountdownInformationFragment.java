package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountdownInformationFragment extends Fragment {

    private TextView ortTextView;
    private TextView startZeitTextView;
    private TextView teilnehmerTextView;

    private String ort;
    private String zeit;
    private String teilnehmerZahl;

    public CountdownInformationFragment() {
        // Required empty public constructor
    }

    public static CountdownInformationFragment newInstance() {
        CountdownInformationFragment fragment = new CountdownInformationFragment();
        Bundle args = new Bundle();

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
        // init Views
        initViews(view);

        // fill Views
        fillViews();
    }

    // Gets Views from IDs
    private void initViews(View view){
        ortTextView = view.findViewById(R.id.meeting_ort_text_view);
        startZeitTextView = view.findViewById(R.id.meeting_start_zeit_text_view);
        teilnehmerTextView = view.findViewById(R.id.meeting_teilnehmer_text_view);
    }

    // Get the values for the TextViews
    public void getValuesForTextViews(String ort, String zeit, String teilnehmerZahl){
        // Save values localy cause TexViews are still null at this point
        this.ort = ort;
        this.zeit = zeit;
        this.teilnehmerZahl = teilnehmerZahl;
    }

    private void fillViews(){
        ortTextView.setText(ort);
        startZeitTextView.setText(zeit);
        teilnehmerTextView.setText(teilnehmerZahl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countdown_information, container, false);
    }
}