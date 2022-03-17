package com.example.myapplication.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

public class OnboardingActivity extends AppCompatActivity {

    private CarouselView carouselView;
    // private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        getSupportActionBar().hide();

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(3);
        carouselView.setViewListener(position -> {
            View view = getLayoutInflater().inflate(R.layout.onboarding_carousel_page, null);
            TextView textView = view.findViewById(R.id.onboarding_carousel_page_textView);
            textView.setText("OnboardingActivity Page " + (position + 1));
            return view;
        });

        // getStartedButton = findViewById(R.id.onboarding_botton_get_started);
    }

    public void onGetStartedButtonClicked(View view) {
        finish();
    }
}