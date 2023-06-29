package com.nam.keep.model;

import android.graphics.Bitmap;

import java.util.List;

public class Note {
    private long id;
    private int index;
    private String title;
    private String content;
    private int isCheckBoxOrContent;
    private String deadline;
    private int color;
    private String background;
    private String updatedAt;
    private long userId;
    private int isSync;
    private int archive;

    // api
    private String is_check_box_or_content;
    private String user_id;
    private String created_at;
    private String updated_at;
    private List<User> users;

    public Note() {
    }

    public Note(int index, String title, String content, int isCheckBoxOrContent, String deadline, int color, String background, String updatedAt, long userId) {
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

    public Note(long id, int index, String title, String content, int isCheckBoxOrContent, String deadline, int color, String background, String updatedAt) {
        this.id = id;
        this.index = index;
        this.title = title;
        this.content = content;
        this.isCheckBoxOrContent = isCheckBoxOrContent;
        this.deadline = deadline;
        this.color = color;
        this.background = background;
        this.updatedAt = updatedAt;
    }

    public Note(long id, int index, String title, String content, int isCheckBoxOrContent, String deadline, int color, String background, String updatedAt, long userId) {
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
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

    public String getIs_check_box_or_content() {
        return is_check_box_or_content;
    }

    public void setIs_check_box_or_content(String is_check_box_or_content) {
        this.is_check_box_or_content = is_check_box_or_content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getArchive() {
        return archive;
    }

    public void setArchive(int archive) {
        this.archive = archive;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
