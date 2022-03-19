package com.example.myapplication.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class MeetingParticipantPair {
        @Embedded
        MeetingData meeting;
        @Relation(
                parentColumn = "meetingID",
                entity = ParticipantData.class,
                entityColumn = "participantID",
                associateBy = @Junction(
                        value = MeetingWithParticipantData.class,
                        parentColumn = "meetingID",
                        entityColumn = "participantID")
        )
        List<ParticipantData> participants;
}