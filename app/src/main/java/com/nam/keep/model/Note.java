package com.nam.keep.model;

import android.graphics.Bitmap;

public class Note {
    private long id;
    private int index;
    private String title;
    private String content;
    private int isCheckBoxOrContent;
    private String deadline;
    private int color;
    private byte[] background;
    private String updatedAt;
    private long userId;

    public Note() {
    }

    public Note(int index, String title, String content, int isCheckBoxOrContent, String deadline, int color, byte[] background, String updatedAt, long userId) {
        this.index = index;
        this.title = title;
        this.content = content;
        this.isCheckBoxOrContent = isCheckBoxOrContent;
        this.deadline = deadline;
        this.color = color;
        this.background = background;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public Note(long id, int index, String title, String content, int isCheckBoxOrContent, String deadline, int color, byte[] background, String updatedAt, long userId) {
        this.id = id;
        this.index = index;
        this.title = title;
        this.content = content;
        this.isCheckBoxOrContent = isCheckBoxOrContent;
        this.deadline = deadline;
        this.color = color;
        this.background = background;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
