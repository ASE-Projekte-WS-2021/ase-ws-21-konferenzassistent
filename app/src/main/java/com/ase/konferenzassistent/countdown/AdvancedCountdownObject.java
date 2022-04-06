package com.ase.konferenzassistent.countdown;

import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class AdvancedCountdownObject implements Serializable {
    String mCountdownName;
    Boolean mEnabled;
    ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> mItems;

    public AdvancedCountdownObject(String mCountdownName,
                                   Boolean mEnabled,
                                   ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> mItems) {
        this.mCountdownName = mCountdownName;
        this.mEnabled = mEnabled;
        this.mItems = mItems;
    }

    public String getmCountdownName() {
        return mCountdownName;
    }

    public void setmCountdownName(String mCountdownName) {
        this.mCountdownName = mCountdownName;
    }

    public Boolean getmEnabled() {
        return mEnabled;
    }

    public void setmEnabled(Boolean mEnabled) {
        this.mEnabled = mEnabled;
    }

    public ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> mItems) {
        this.mItems = mItems;
    }
}

