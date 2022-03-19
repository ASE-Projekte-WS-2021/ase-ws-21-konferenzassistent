package com.example.myapplication.meetingwizard;

import java.io.Serializable;

public class Participant implements Serializable {

    Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Participant(String name, String status, Boolean isSelected, Integer id) {
        this.id = id;
        Name = name;
        Status = status;
        this.isSelected = isSelected;
    }
}
