package com.example.myapplication.meetingwizard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.CountdownActivity;
import com.example.myapplication.MeetingHistoryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.checklist.ChecklistAdapter;
import com.example.myapplication.checklist.ChecklistItem;
import com.example.myapplication.checklist.OnAdapterItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WizardChecklistFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class WizardChecklistFragment extends Fragment {

    private int checkedItems;

    private RecyclerView rvChecklist;
    private List<ChecklistItem> checklistItems;
    private ChecklistAdapter checklistAdapter;
    private LinearLayoutManager linearLayoutManager;

    OnAdapterItemClickListener listener;
    // TODO: Rename and change types and number of parameters
    public static WizardChecklistFragment newInstance() {
        return new WizardChecklistFragment();
    }

    public WizardChecklistFragment() {
        // Required empty public constructor
    }
    public WizardChecklistFragment(OnAdapterItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChecklist = view.findViewById(R.id.rv_checklist_wizard);

        // initialize checklist and recyclerview
        checklistItems = new ArrayList<>();
        checklistItems.add(new ChecklistItem("Desinfektionsmittel bereit"));
        checklistItems.add(new ChecklistItem("3G-Regelung o.ä. geprüft"));
        checklistItems.add(new ChecklistItem("Masken / Plexiglas geprüft"));
        checklistItems.add(new ChecklistItem("Abstände gewährleistet"));


        checklistAdapter = new ChecklistAdapter(this.getContext(), listener, checklistItems);
        rvChecklist.setAdapter(checklistAdapter);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvChecklist.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //check checklist
    public void checkItem() {
        checkedItems = 0;
        for (ChecklistItem item : checklistItems) {
            if (item.isChecked()) {
                checkedItems++;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wizard_checklist, container, false);
    }
}
