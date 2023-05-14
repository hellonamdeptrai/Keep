package com.nam.keep.model;

public class CheckBoxContentNote {
    private String content;
    private boolean checkBox;

    public CheckBoxContentNote() {
    }

    public CheckBoxContentNote(String content, boolean checkBox) {
        this.content = content;
        this.checkBox = checkBox;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }
}
