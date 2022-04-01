package com.ase.konferenzassistent.mainscreen.recycleviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.mainscreen.settings.EditItemBottomSheet;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.util.ArrayList;

public class RecyclerItemPresetAdapter
        extends RecyclerView.Adapter<RecyclerItemPresetAdapter.ViewHolder>
        implements EditItemBottomSheet.timerEdit {

    private final Context mContext;
    private final ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> countdownItem;
    EditItemBottomSheet editItemBottomSheet;

    public RecyclerItemPresetAdapter(
            ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> countdownItem,
            Context mContext) {
        this.countdownItem = countdownItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_countdown_item_creation,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.presetName.setText(countdownItem.get(position).getSubCountdownDescription());

        holder.itemContainer.setOnClickListener(view -> {
            // Open Editor
            editItemBottomSheet = new EditItemBottomSheet();
            editItemBottomSheet.initTimer(countdownItem.get(position), this, position);
            editItemBottomSheet.show(((AppCompatActivity) mContext).getSupportFragmentManager(), editItemBottomSheet.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return countdownItem.size();
    }

    @Override
    public void onEditFinish(RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem mItem, int position) {
        countdownItem.get(position).setSubCountdown(mItem.getSubCountdown());
        countdownItem.get(position).setSubCountdownDescription(mItem.getSubCountdownDescription());
        editItemBottomSheet.closeCreation();
        notifyDataSetChanged();
    }


    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView presetName;
        RelativeLayout itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presetName = itemView.findViewById(R.id.item_description);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
