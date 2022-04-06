package com.ase.konferenzassistent.meetingwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.checklist.ChecklistAdapter;
import com.ase.konferenzassistent.checklist.ChecklistItem;
import com.ase.konferenzassistent.checklist.OnAdapterItemClickListener;

import java.util.ArrayList;

public class WizardChecklistFragment extends Fragment {

    private final ArrayList<ChecklistItem> checklistItems = new ArrayList<>();
    OnAdapterItemClickListener listener;
    private int checkedItems;

    public WizardChecklistFragment() {
        // Required empty public constructor
    }

    public WizardChecklistFragment(OnAdapterItemClickListener listener, ArrayList<ChecklistItem> checklistItems) {
        this.checklistItems.addAll(checklistItems);
        this.listener = listener;
    }

    public void setCheckedItems(int checkedItems) {
        this.checkedItems = checkedItems;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvChecklist = view.findViewById(R.id.rv_checklist_wizard);

        ChecklistAdapter checklistAdapter = new ChecklistAdapter(this.getContext(), listener, checklistItems);
        rvChecklist.setAdapter(checklistAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
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
