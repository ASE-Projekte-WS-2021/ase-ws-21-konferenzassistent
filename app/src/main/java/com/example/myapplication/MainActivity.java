package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private TextView actionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Disables Night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Hide the action bar, we only want to show it once the user scrolls down
        Objects.requireNonNull(getSupportActionBar()).hide();

        setUpMenu();
    }

    // Setup the Bottom Menu
    private void setUpMenu(){
        // Get the Navigation View and the Navigation Controller
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);


        // Setup Navigation Controller
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configure App Bar to Change
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.miHome, R.id.miSettings).build();

        // Setup the App Bar with the Navigation Controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // initialize the custom action bar header
        actionBarText = findViewById(R.id.main_bar_title);

        // add listener for fragment change
        NavController.OnDestinationChangedListener listener =
                ((controller, navDestination, bundle) -> {
                    rebuildActionBar();
                } );
        navController.addOnDestinationChangedListener(listener);

    }

    // Edits the custom actionbar for every Fragment
    private void rebuildActionBar(){
        actionBarText.setText(Objects.requireNonNull(getSupportActionBar()).getTitle());
    }

    // Opens the Meeting Wizard
    public void openMeetingWizard(View view) {

        // creates a Bottom sheet to create a meeting
        CreateMeetingBottomSheetAdapter createMeetingBottomSheetAdapter = new CreateMeetingBottomSheetAdapter();
        createMeetingBottomSheetAdapter.show(getSupportFragmentManager() , createMeetingBottomSheetAdapter.getTag());

    }

    public void startMeetingWizard(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}