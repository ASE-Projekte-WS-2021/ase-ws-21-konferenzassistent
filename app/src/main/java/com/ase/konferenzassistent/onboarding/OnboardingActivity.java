package com.ase.konferenzassistent.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.ase.konferenzassistent.R;
import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnboardingActivity extends AppCompatActivity {

    // private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Objects.requireNonNull(getSupportActionBar()).hide();

        List<OnboardingPageData> onboardingPages = new ArrayList<>();
        onboardingPages.add(new OnboardingPageData(
                getString(R.string.onboarding_page_1_headline),
                getString(R.string.onboarding_page_1_subhead),
                R.drawable.ic_outline_group_24));
        onboardingPages.add(new OnboardingPageData(
                getString(R.string.onboarding_page_2_headline),
                getString(R.string.onboarding_page_2_subhead),
                R.drawable.ic_outline_notifications_active_24));
        onboardingPages.add(new OnboardingPageData(
                getString(R.string.onboarding_page_3_headline),
                getString(R.string.onboarding_page_3_subhead),
                R.drawable.ic_outline_history_24));


        CarouselView carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(onboardingPages.size());
        carouselView.setViewListener(position -> {
            View view = getLayoutInflater().inflate(R.layout.onboarding_carousel_page, null);

            TextView textViewHeadline = view.findViewById(R.id.onboarding_carousel_page_headline);
            TextView textViewSubhead = view.findViewById(R.id.onboarding_carousel_page_subhead);
            ImageView imageView = view.findViewById(R.id.onboarding_carousel_page_imageView);

            OnboardingPageData onboardingPage = onboardingPages.get(position);
            textViewHeadline.setText(onboardingPage.getHeadline());
            textViewSubhead.setText(onboardingPage.getSubhead());
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), onboardingPage.getDrawableID(), getTheme()));

            return view;
        });

        // getStartedButton = findViewById(R.id.onboarding_botton_get_started);
    }

    public void onGetStartedButtonClicked(View view) {
        finish();
    }
}