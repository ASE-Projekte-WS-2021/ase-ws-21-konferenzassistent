package com.example.myapplication.data.presets.countdown;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.myapplication.PresetPair;

import java.util.List;

public class CountdownPresetPair implements PresetPair {
        @Embedded
        CountdownPresetData presets;
        @Relation(
                parentColumn = "ID",
                entity = CountdownParentData.class,
                entityColumn = "ID",
                associateBy = @Junction(
                        value = CountdownPresetWithParentData.class,
                        parentColumn = "presetID",
                        entityColumn = "countdownParentID")
        )
        List<CountdownParentData> parents;

        public CountdownPresetData getPresets() {
                return presets;
        }

        public void setPresets(CountdownPresetData presets) {
                this.presets = presets;
        }

        public List<CountdownParentData> getParents() {
                return parents;
        }

        public void setParents(List<CountdownParentData> parents) {
                this.parents = parents;
        }
}