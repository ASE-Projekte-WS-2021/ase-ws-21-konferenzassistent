package com.ase.konferenzassistent.mainscreen.recycleviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.checklist.ChecklistItem;

import java.util.ArrayList;

public class RecyclerViewCreatedChecklistAdapter
        extends RecyclerView.Adapter<RecyclerViewCreatedChecklistAdapter.ViewHolder> {

    private final ArrayList<ChecklistItem> checklistItems;

    public RecyclerViewCreatedChecklistAdapter(
            ArrayList<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set Values or Android will use old ones
        holder.checklistHint.setText(checklistItems.get(holder.getAdapterPosition()).getHint());
        holder.checklistName.setText(checklistItems.get(holder.getAdapterPosition()).getTitle());

        // Sets the Hint of the checklist item
        holder.checklistHint.setOnEditorActionListener((textView, i, keyEvent) -> {
            checklistItems.get(holder.getAdapterPosition()).setHint(textView.getText().toString());
            return false;
        });

        // Makes sure the ChecklistName gets changed
        holder.checklistName.setOnFocusChangeListener((view, b) ->
                checklistItems.get(holder.getAdapterPosition()).setTitle(
                        holder.checklistName.getText().toString()));

        // Makes sure the ChecklistHint gets changed
        holder.checklistHint.setOnFocusChangeListener((view, b) ->
                checklistItems.get(holder.getAdapterPosition()).setHint(
                        holder.checklistHint.getText().toString()));

        // Sets the Title of the checklist item
        holder.checklistName.setOnEditorActionListener((textView, i, keyEvent) -> {
            checklistItems.get(holder.getAdapterPosition()).setTitle(textView.getText().toString());
            return false;
        });

        // Deletes Checklist
        holder.buttonDelete.setOnClickListener(view -> {
            holder.checklistName.clearFocus();
            holder.checklistHint.clearFocus();
            checklistItems.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_checklist_add,
                parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText checklistName;
        EditText checklistHint;
        LinearLayout buttonDelete;

        public ViewHolder(View view) {
            super(view);
            buttonDelete = view.findViewById(R.id.remove_item);
            checklistName = view.findViewById(R.id.checklist_name);
            checklistHint = view.findViewById(R.id.checklist_hint);
        }
    }
}
