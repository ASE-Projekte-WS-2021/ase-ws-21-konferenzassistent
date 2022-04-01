package com.example.myapplication.data;

import androidx.room.Entity;

@Entity(primaryKeys = {"meetingID", "participantID"}, tableName = "table_meeting_with_participant_data")
public class MeetingWithParticipantData {

    private int meetingID;
    private int participantID;

    public int getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }

    public int getParticipantID() {
        return participantID;
    }

    public void setParticipantID(int participantID) {
        this.participantID = participantID;
    }
}
