package com.example.myapplication;

public class Meeting {

    private String id, date, dateEnd, location, duration, numberParticipants;

    public Meeting(String id, String date, String dateEnd, String location, String duration, String numberParticipants) {
        this.id = id;
        this.date = date;
        this.dateEnd = dateEnd;
        this.location = location;
        this.duration = duration;
        this.numberParticipants = numberParticipants;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumberParticipants() {
        return numberParticipants;
    }

    public void setNumberParticipants(String numberParticipants) {
        this.numberParticipants = numberParticipants;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", location='" + location + '\'' +
                ", duration='" + duration + '\'' +
                ", numberParticipants='" + numberParticipants + '\'' +
                '}';
    }
}
