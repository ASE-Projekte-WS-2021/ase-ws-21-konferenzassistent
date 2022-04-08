package com.ase.konferenzassistent.meetingwizard;

import java.io.Serializable;

public class Participant implements Serializable {

    Integer id;
    String Name;
    String email;
    String Status;
    Boolean isSelected;

    public Participant(String name, String email, String status, Boolean isSelected, Integer id) {
        this.id = id;
        this.email = email;
        Name = name;
        Status = status;
        this.isSelected = isSelected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
