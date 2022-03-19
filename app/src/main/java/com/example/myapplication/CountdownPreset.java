package com.example.myapplication;

import com.example.myapplication.data.RoomDB;
import com.example.myapplication.data.presets.countdown.CountdownItemPair;
import com.example.myapplication.data.presets.countdown.CountdownPresetPair;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.util.ArrayList;

public class CountdownPreset {
    String title;
    ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObject;

    public CountdownPreset(String title, ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObject) {
        this.title = title;
        this.advancedCountdownObject = advancedCountdownObject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> getAdvancedCountdownObject() {
        return advancedCountdownObject;
    }

    public void setAdvancedCountdownObject(ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObject) {
        this.advancedCountdownObject = advancedCountdownObject;
    }

    public static ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> convertToAdvancedCowntdownList(RoomDB database, CountdownPresetPair presetPair){
        ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject>  list = new ArrayList<>();
        presetPair.getParents().forEach(parents ->{
            String countdownName = parents.getTitle();

            ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> children =
                    new ArrayList<>();

            RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject advancedCountdownObject =
                    new RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject(countdownName, true, children);

            CountdownItemPair childItems = database.countdownParentWIthItemDao().getSingularCountdowns(parents.getID());
            childItems.getCountdownItems().forEach(itemData -> {
                Long countdown = itemData.getCountdown();
                String countdownDescription = itemData.getDescription();

                RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem child =
                        new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem(
                                countdown,
                                countdownDescription);
                children.add(child);

            });


            list.add(advancedCountdownObject);
        });

        return list;
    }

}
