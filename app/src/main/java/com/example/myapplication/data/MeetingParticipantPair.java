package com.example.myapplication.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class MeetingParticipantPair {
        @Embedded
        MeetingData meeting;
        @Relation(
                parentColumn = "ID",
                entity = ParticipantData.class,
                entityColumn = "ID",
                associateBy = @Junction(
                        value = MeetingWithParticipantData.class,
                        parentColumn = "meetingID",
                        entityColumn = "participantID")
        )
        List<ParticipantData> participants;

        public MeetingData getMeeting() {
                return meeting;
        }

        public void setMeeting(MeetingData meeting) {
                this.meeting = meeting;
        }

        public List<ParticipantData> getParticipants() {
                return participants;
        }

        public void setParticipants(List<ParticipantData> participants) {
                this.participants = participants;
        }
}