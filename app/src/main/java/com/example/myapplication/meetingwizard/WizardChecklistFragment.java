package com.example.myapplication.meetingwizard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.checklist.ChecklistAdapter;
import com.example.myapplication.checklist.ChecklistItem;
import com.example.myapplication.checklist.OnAdapterItemClickListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WizardChecklistFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class WizardChecklistFragment extends Fragment {

    public int getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(int checkedItems) {
        this.checkedItems = checkedItems;
    }

    private int checkedItems;

    private RecyclerView rvChecklist;
    private final ArrayList<ChecklistItem> checklistItems = new ArrayList<>();
    private ChecklistAdapter checklistAdapter;
    private LinearLayoutManager linearLayoutManager;

    OnAdapterItemClickListener listener;
    public WizardChecklistFragment newInstance() {
        return new WizardChecklistFragment();
    }

    public WizardChecklistFragment() {
        // Required empty public constructor
    }
    public WizardChecklistFragment(OnAdapterItemClickListener listener, ArrayList<ChecklistItem> checklistItems){
        this.checklistItems.addAll(checklistItems);
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChecklist = view.findViewById(R.id.rv_checklist_wizard);

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
    public int checkItem() {
        checkedItems = 0;
        for (ChecklistItem item : checklistItems) {
            if (item.isChecked()) {
                checkedItems++;
            }
        }

        return checkedItems;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wizard_checklist, container, false);
    }
}
