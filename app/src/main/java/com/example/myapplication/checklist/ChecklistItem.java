package com.example.myapplication.checklist;

public class ChecklistItem {

    private String title, hint;
    private boolean isChecked;

    public ChecklistItem(String title) {
        this.title = title;
        this.hint = "";
        this.isChecked = false;
    }

    public ChecklistItem(String title, String hint) {
        this.title = title;
        this.hint = hint;
        this.isChecked = false;
    }

    public ChecklistItem(String title, String hint, boolean isChecked) {
        this.title = title;
        this.hint = hint;
        this.isChecked = isChecked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void toggle() {
        isChecked = !isChecked;
    }

    @Override
    public String toString() {
        return "ChecklistItem{" +
                "title='" + title + '\'' +
                ", hint='" + hint + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
