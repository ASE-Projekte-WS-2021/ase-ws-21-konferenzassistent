package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

// Custom Number picker
//  https://stackoverflow.com/a/14174312
public class CustomNumberPicker extends NumberPicker {

    public CustomNumberPicker(Context context) {
        super(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
    }
    private void processAttributeSet(AttributeSet attrs) {
        //This method reads the parameters given in the xml file and sets the properties according to it
        this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));

        // Sets the Prefix, makes sure to differentiate between plural and singular
        this.setFormatter(i -> i > 1? i + " Minuten" : i +" Minute");

        // Fix for bug in Android Picker where the first element is not shown
        // https://stackoverflow.com/a/44949069
        View firstItem = this.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

}

