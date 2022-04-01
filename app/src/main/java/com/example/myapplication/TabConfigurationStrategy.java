package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// Strategy for the Countdown Tab Layout
public class TabConfigurationStrategy implements TabLayoutMediator.TabConfigurationStrategy {

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        // Sets the right Tab Text
        if (position == 1)
            tab.setText("Informationen");
        else
            tab.setText("Timer");
    }
}
