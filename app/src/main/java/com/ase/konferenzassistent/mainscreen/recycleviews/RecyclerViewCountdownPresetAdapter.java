package com.ase.konferenzassistent.mainscreen.recycleviews;

import static com.ase.konferenzassistent.mainscreen.settings.PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.mainscreen.settings.PresetAddBottomSheet;
import com.ase.konferenzassistent.mainscreen.settings.PresetEditBottomSheet;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.shared.Interfaces.Preset;
import com.ase.konferenzassistent.shared.presets.ChecklistPreset;
import com.ase.konferenzassistent.shared.presets.CountdownPreset;

import java.util.ArrayList;

public class RecyclerViewCountdownPresetAdapter
        extends RecyclerView.Adapter<RecyclerViewCountdownPresetAdapter.ViewHolder> implements PresetAddBottomSheet.editingDone {

    private final ArrayList<? extends Preset> preset;
    private final Context mContext;
    private final Integer viewType;
    PresetEditBottomSheet parent;

    public RecyclerViewCountdownPresetAdapter(
            ArrayList<? extends Preset> preset,
            Context mContext,
            Integer viewType,
            PresetEditBottomSheet parent) {
        this.preset = preset;
        this.mContext = mContext;
        this.viewType = viewType;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_preset_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.presetName.setText(preset.get(holder.getAdapterPosition()).getTitle());

        holder.selectIndicator.setVisibility(View.VISIBLE);
        holder.selectIndicator.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_edit_24, null));

        // create button event
        holder.itemContainer.setOnClickListener(viewListener -> {
            PresetAddBottomSheet presetAddBottomSheet = new PresetAddBottomSheet();
            presetAddBottomSheet.getListener(this);
            presetAddBottomSheet.setVariables(viewType, preset.get(holder.getAdapterPosition()), parent, holder.getAdapterPosition());
            presetAddBottomSheet.show(((AppCompatActivity)mContext).getSupportFragmentManager()
                    , presetAddBottomSheet.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return preset.size();
    }

    @Override
    public void onEditingDone(Preset preset) {
        if(viewType.equals(PRESET_TYPE_COUNTDOWN)){
            CountdownPreset.updateCountdownDatabaseEntry(RoomDB.getInstance(mContext), (CountdownPreset) preset);
        }
        else {
            ChecklistPreset.updateChecklistDatabaseEntry(RoomDB.getInstance(mContext), (ChecklistPreset)preset);
        }
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView presetName;
        final ImageView selectIndicator;
        final RelativeLayout itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presetName = itemView.findViewById(R.id.item_name);
            selectIndicator = itemView.findViewById(R.id.item_select_indicator);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
