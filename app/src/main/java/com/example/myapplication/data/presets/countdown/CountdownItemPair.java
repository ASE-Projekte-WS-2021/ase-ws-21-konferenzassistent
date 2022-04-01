package com.example.myapplication.data.presets.countdown;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CountdownItemPair {
    @Embedded
    CountdownParentData parentCountdown;
    @Relation(
            parentColumn = "ID",
            entity = CountdownItemData.class,
            entityColumn = "ID",
            associateBy = @Junction(
                    value = CountdownParentWithItemData.class,
                    parentColumn = "countdownParentID",
                    entityColumn = "countdownItemID")
    )

    List<CountdownItemData> countdownItems;

    public CountdownParentData getParentCountdown() {
        return parentCountdown;
    }

    public void setParentCountdown(CountdownParentData parentCountdown) {
        this.parentCountdown = parentCountdown;
    }

    public List<CountdownItemData> getCountdownItems() {
        return countdownItems;
    }

    public void setCountdownItems(List<CountdownItemData> countdownItems) {
        this.countdownItems = countdownItems;
    }
}