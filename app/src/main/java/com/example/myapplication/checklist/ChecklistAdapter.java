package com.example.myapplication.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private Context ct;
    private List<ChecklistItem> checklistItems;
    private OnAdapterItemClickListener adapterItemClickListener = null;

    public ChecklistAdapter(Context ct, List<ChecklistItem> checklistItems) {
        this.ct = ct;
        this.checklistItems = checklistItems;
        this.adapterItemClickListener = (OnAdapterItemClickListener) ct;
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.rv_row_checklist, parent, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {
        holder.checkBoxItem.setText(checklistItems.get(position).getTitle());
        holder.checkBoxItem.setChecked(checklistItems.get(position).isChecked());
        holder.cardViewItem.setOnClickListener(view -> {
            checklistItems.get(position).toggle();
            holder.checkBoxItem.toggle();
            adapterItemClickListener.onAdapterItemClickListener();
        });
        String hint = checklistItems.get(position).getHint();
        if (hint.equals("")) {
            holder.infoButtonItem.setVisibility(View.GONE);
        } else {
            holder.infoButtonItem.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ct)
                        .setMessage(hint)
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            // do nothing
                        });
                builder.create().show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewItem;
        CheckBox checkBoxItem;
        ImageButton infoButtonItem;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewItem = itemView.findViewById(R.id.rv_row_checklist_cardview);
            checkBoxItem = itemView.findViewById(R.id.rv_row_checklist_cb);
            infoButtonItem = itemView.findViewById(R.id.rv_row_checklist_info);
        }
    }
}
