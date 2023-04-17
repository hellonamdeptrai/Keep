package com.nam.keep.model;

public class Note {
    private int id;
    private String title;
    private String content;
    private int isCheckBoxOrContent;
    private int color;
    private byte[] background;
    private int categoryId;

    public Note(int id, String title, String content, int isCheckBoxOrContent, int color, byte[] background, int categoryId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isCheckBoxOrContent = isCheckBoxOrContent;
        this.color = color;
        this.background = background;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsCheckBoxOrContent() {
        return isCheckBoxOrContent;
    }

    public void setIsCheckBoxOrContent(int isCheckBoxOrContent) {
        this.isCheckBoxOrContent = isCheckBoxOrContent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
