package com.ase.konferenzassistent.countdown;

import androidx.annotation.NonNull;

import com.ase.konferenzassistent.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// Strategy for the Countdown Tab Layout
public class TabConfigurationStrategy implements TabLayoutMediator.TabConfigurationStrategy {

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        // Sets the right Tab Text
        if (position == 1)
            tab.setText(R.string.informationen_tab);
        else
            tab.setText(R.string.timer_tab);
    }
}
