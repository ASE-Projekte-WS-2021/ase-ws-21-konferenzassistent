package com.ase.konferenzassistent.onboarding;

import androidx.annotation.NonNull;

public class OnboardingPageData {

    private String headline, subhead;
    private int drawableID;

    public OnboardingPageData(String headline, String subhead, int drawableID) {
        this.headline = headline;
        this.subhead = subhead;
        this.drawableID = drawableID;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    @NonNull
    @Override
    public String toString() {
        return "OnboardingPageData{" +
                "headline='" + headline + '\'' +
                ", subhead='" + subhead + '\'' +
                ", drawableID=" + drawableID +
                '}';
    }
}
