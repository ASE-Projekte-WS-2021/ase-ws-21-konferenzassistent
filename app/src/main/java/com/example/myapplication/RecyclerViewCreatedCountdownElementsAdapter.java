package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.myapplication.meetingwizard.LinePagerIndicatorDecoration;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerViewCreatedCountdownElementsAdapter
        extends RecyclerView.Adapter<RecyclerViewCreatedCountdownElementsAdapter.ViewHolder>{

    private final Context mContext;
    private final ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> mAdvancedCountdownObjects;

    private ArrayList<RecyclerItemPresetAdapter> presetAdapters = new ArrayList<>();
    private ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> advancedCountdownItems = new ArrayList();
    private ArrayList<Boolean> contentHidden = new ArrayList<>();

    public RecyclerViewCreatedCountdownElementsAdapter(
            ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> mAdvancedCountdownObjects,
            Context mContext)
    {
        this.mAdvancedCountdownObjects = mAdvancedCountdownObjects;
        this.mContext = mContext;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("TAG", "onBindViewHolder: " +contentHidden.size() + position);
        buildRecyclerView(holder, position);

        if(contentHidden.size() -1 < position){
            contentHidden.add(true);
        }
        holder.countdownContainer.setVisibility(!contentHidden.get(position)? View.VISIBLE: View.GONE);

        holder.buttonCreate.setOnClickListener(view -> {
            mAdvancedCountdownObjects.get(position).getmItems().add(
                    new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem((long)5, "hi"));
            presetAdapters.get(position).notifyItemInserted(mAdvancedCountdownObjects.get(position).getmItems().size());
        });

        holder.buttonHide.setOnClickListener(view -> {
            if(!contentHidden.get(position)){
                holder.countdownContainer.setVisibility(View.GONE);
                holder.buttonHide.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_keyboard_arrow_down_24, null));
                contentHidden.set(position, true);
            }
            else
            {
                holder.countdownContainer.setVisibility(View.VISIBLE);
                holder.buttonHide.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_keyboard_arrow_up_24, null));
                ViewCompat.setBackgroundTintList(holder.buttonHide, ColorStateList.valueOf( mContext.getResources().getColor(R.color.black)));
                contentHidden.set(position, false);
            }

        });

        // Load the Child recycler view
        /*
        LinearLayoutManager childView = new LinearLayoutManager(holder.recyclerView.getContext(),
                RecyclerView.HORIZONTAL, false);

        RecyclerViewAdvancedCountdownItemAdapter recyclerViewCountdownAdapter= new RecyclerViewAdvancedCountdownItemAdapter(
                getmAdvancedCountdownObjects().get(holder.getAdapterPosition()).mItems,
                mContext);

        holder.recyclerView.setAdapter(recyclerViewCountdownAdapter);
        holder.recyclerView.setLayoutManager(childView);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(holder.recyclerView);
        if(getmAdvancedCountdownObjects().get(holder.getAdapterPosition()).mItems.size() > 1)
            holder.recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        */

        // Setup the container visibility
        /*
        holder.countdownContainer.setVisibility(holder.aSwitch.isChecked()? View.VISIBLE : View.GONE);

        // Set on click listener on the switch
        holder.aSwitch.setOnClickListener(view1 -> {
            if(!holder.aSwitch.isChecked()){
                mAdvancedCountdownObjects.get(holder.getAdapterPosition()).mEnabled = false;
                holder.countdownContainer.setVisibility(View.GONE);
            }
            else{
                mAdvancedCountdownObjects.get(holder.getAdapterPosition()).mEnabled = true;
                holder.countdownContainer.setVisibility(View.VISIBLE);
            }
        });
        */

    }




    @Override
    public int getItemCount() {
        return mAdvancedCountdownObjects.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_countdown_add,
                parent, false);
        return new ViewHolder(view);
    }

    private void buildRecyclerView(ViewHolder holder, int position){


        RecyclerView recyclerView = holder.recyclerView;
        RecyclerItemPresetAdapter adapter = new RecyclerItemPresetAdapter(
                mAdvancedCountdownObjects.get(position).getmItems(),
                mContext
                );
        presetAdapters.add(adapter);
        Log.i("TAG", "buildRecyclerView: " + presetAdapters);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText countdownName;
        LinearLayout countdownContainer;
        RecyclerView recyclerView;
        ImageView buttonHide;
        LinearLayout buttonCreate;

        public ViewHolder(View countdownView){
            super(countdownView);
            buttonCreate = countdownView.findViewById(R.id.button_create);
            countdownName = countdownView.findViewById(R.id.countdown_advanced_recycler_name);
            countdownContainer = countdownView.findViewById(R.id.add_countdown_container);
            recyclerView = countdownView.findViewById(R.id.item_recycle_view);
            buttonHide = countdownView.findViewById(R.id.button_hide_countdown);
    }

}


}
