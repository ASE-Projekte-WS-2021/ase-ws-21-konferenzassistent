package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

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
        // Fixes Android using old values
        holder.countdownName.setText(
                mAdvancedCountdownObjects.get(holder.getAdapterPosition()).getmCountdownName());

        buildRecyclerView(holder, position);


        if(contentHidden.size() -1 < position){
            contentHidden.add(true);
        }
        holder.countdownContainer.setVisibility(!contentHidden.get(position)? View.VISIBLE: View.GONE);


        // Create new Item
        holder.buttonCreate.setOnClickListener(view -> {
            mAdvancedCountdownObjects.get(holder.getAdapterPosition()).getmItems().add(
                    new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem((long)5, ""));
            presetAdapters.get(position).notifyItemInserted(mAdvancedCountdownObjects.get(holder.getAdapterPosition()).getmItems().size()-1);
        });

        // Hides the ui
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

        holder.countdownName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    mAdvancedCountdownObjects.get(holder.getAdapterPosition()).setmCountdownName(textView.getText().toString());
                }
                return false;
            }
        });

        // Deletes Countdown
        holder.buttonDelete.setOnClickListener(view -> {
            holder.countdownName.clearFocus();
            contentHidden.remove(holder.getAdapterPosition());
            presetAdapters.remove(holder.getAdapterPosition());
            mAdvancedCountdownObjects.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });


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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText countdownName;
        LinearLayout countdownContainer;
        RecyclerView recyclerView;
        ImageView buttonHide;
        LinearLayout buttonCreate;
        LinearLayout buttonDelete;

        public ViewHolder(View countdownView){
            super(countdownView);
            buttonCreate = countdownView.findViewById(R.id.button_create);
            countdownName = countdownView.findViewById(R.id.countdown_name);
            countdownContainer = countdownView.findViewById(R.id.add_countdown_container);
            recyclerView = countdownView.findViewById(R.id.item_recycle_view);
            buttonHide = countdownView.findViewById(R.id.button_hide_countdown);
            buttonDelete = countdownView.findViewById(R.id.remove_countdown);
    }

}


}
