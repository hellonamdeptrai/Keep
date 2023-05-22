package com.nam.keep.model;

public class Label {
    private long id;
    private String title;

    public Label() {
    }

    public Label(long id) {
        this.id = id;
    }

    public Label(String title) {
        this.title = title;
    }

    public Label(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
