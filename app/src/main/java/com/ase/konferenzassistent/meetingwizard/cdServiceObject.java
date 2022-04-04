package com.ase.konferenzassistent.meetingwizard;

import android.os.CountDownTimer;

import java.io.Serializable;

public class cdServiceObject implements Serializable {

    RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject timer;
    Long currentTime;
    Boolean timerDone;
    Boolean timerRunning;
    Integer countdownPosition;
    Integer countdownID;
    transient CountDownTimer countDownTimer;

    public cdServiceObject(RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject timer, Long currentTime, Boolean timerDone, Integer countdownPosition, Integer countdownID) {
        this.timer = timer;
        this.currentTime = currentTime;
        this.timerDone = timerDone;
        this.countdownPosition = countdownPosition;
        this.countdownID = countdownID;
    }

    public RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject getTimer() {
        return timer;
    }

    public void setTimer(RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject timer) {
        this.timer = timer;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public Boolean getTimerDone() {
        return timerDone;
    }

    public void setTimerDone(Boolean timerDone) {
        this.timerDone = timerDone;
    }

    public Boolean getTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(Boolean timerRunning) {
        this.timerRunning = timerRunning;
    }

    public Integer getCountdownPosition() {
        return countdownPosition;
    }

    public void setCountdownPosition(Integer countdownPosition) {
        this.countdownPosition = countdownPosition;
    }



    public void setCountdownID(Integer countdownID) {
        this.countdownID = countdownID;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }


}
