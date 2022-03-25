package com.example.myapplication;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.NumberPicker;
import android.widget.TimePicker;

// Custom Time Picker Dialog to remove the hours
//https://stackoverflow.com/questions/20214547/show-timepicker-with-minutes-intervals-in-android/20396673#20396673
public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 1; // Time Picker interval
    private TimePicker timePicker;
    private final OnTimeSetListener timeSetListener;

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView){
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay, minute/TIME_PICKER_INTERVAL, is24HourView);
        timeSetListener = listener;
    }

    @Override
    public void updateTime(int hoursOfDay, int minutesOfHour){
        timePicker.setHour(hoursOfDay);
        timePicker.setMinute(minutesOfHour);
    }

    @Override
    public void onClick(DialogInterface dialog, int which){
        switch(which){
            case BUTTON_POSITIVE:
                if(timeSetListener != null){
                    timeSetListener.onTimeSet(timePicker, timePicker.getHour(),timePicker.getMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        try{
            timePicker = (TimePicker) findViewById(Resources.getSystem().getIdentifier("timePicker", "id", "android"));
            NumberPicker hourPicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier("hour", "id", "android"));

            hourPicker.setMaxValue(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
