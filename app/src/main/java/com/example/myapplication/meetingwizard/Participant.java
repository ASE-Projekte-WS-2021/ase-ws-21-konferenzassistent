package com.example.myapplication.meetingwizard;

public class Participant {

    String Name;
    String Status;
    Boolean isSelected;

    // TODO: Add more things ?
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Participant(String name, String status, Boolean isSelected) {
        Name = name;
        Status = status;
        this.isSelected = isSelected;
    }
}
