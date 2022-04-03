package com.ase.konferenzassistent.mainscreen.recycleviews;

import static com.ase.konferenzassistent.mainscreen.settings.PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN;

import android.content.Context;
import android.util.Log;
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
import com.ase.konferenzassistent.mainscreen.settings.PresetAddCountdownBottomSheet;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.shared.Interfaces.Preset;

import java.util.ArrayList;

public class RecyclerViewCountdownPresetAdapter
        extends RecyclerView.Adapter<RecyclerViewCountdownPresetAdapter.ViewHolder> implements CustomAlertBottomSheetAdapter.onLeaveListener, PresetAddCountdownBottomSheet.editingDone {

    private final ArrayList<? extends Preset> preset;
    private final Context mContext;
    private final onDeletionListener listener;
    private final Integer viewType;
    int selectedPosition;

    public RecyclerViewCountdownPresetAdapter(
            ArrayList<? extends Preset> preset,
            onDeletionListener listener,
            Context mContext,
            Integer viewType) {
        this.preset = preset;
        this.mContext = mContext;
        this.listener = listener;
        this.viewType = viewType;
    }

    @Override
    public void onLeaving() {
        listener.onDelete(selectedPosition);
    }

    @Override
    public void clearWarnings() {

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
        holder.selectIndicator.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_delete_forever_24, null));

        // create button event
        holder.itemContainer.setOnClickListener(viewListener -> {
            PresetAddCountdownBottomSheet presetAddCountdownBottomSheet = new PresetAddCountdownBottomSheet();
            presetAddCountdownBottomSheet.getListener(this);
            presetAddCountdownBottomSheet.getViewType(viewType, preset.get(holder.getAdapterPosition()));
            presetAddCountdownBottomSheet.show(((AppCompatActivity)mContext).getSupportFragmentManager()
                    , presetAddCountdownBottomSheet.getTag());
        });


    }

    @Override
    public int getItemCount() {
        return preset.size();
    }

    private void createAlert(String title) {
        if(title.equals("Standard")){
            Toast.makeText(mContext, "Die Standardeinstellungen können nicht gelöscht werden",
                    Toast.LENGTH_LONG).show();
            return;
        }
        CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
        customAlertBottomSheetAdapter.setWarningText("Soll das Preset \"" + title + "\" wirklich gelöscht werden."); // Mitteilung
        customAlertBottomSheetAdapter.setAcceptText("Preset löschen"); // Positives Feedback
        customAlertBottomSheetAdapter.setDeclineText("Preset behalten"); // Negatives Feedback
        customAlertBottomSheetAdapter.show(((AppCompatActivity) mContext).getSupportFragmentManager(), customAlertBottomSheetAdapter.getTag());
    }

    @Override
    public void onEditingDone(Preset preset) {
        if(viewType.equals(PRESET_TYPE_COUNTDOWN)){
            // TODO: update database entry with id
        }
        else {
            // TODO: update database entry with id
        }
        Log.i("TAG", "onEditingDone: "+ preset.getID());
    }

    public interface onDeletionListener {
        void onDelete(int position);
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
