package com.nam.keep.model;

import java.util.List;

public class AllData {
    private List<User> users;
    private List<Note> notes;
    private List<FileModel> images;
    private List<FileModel> files;
    private List<Label> labels;
    private List<NoteHas> note_has_label;
    private List<NoteHas> note_has_user;


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<FileModel> getImages() {
        return images;
    }

    public void setImages(List<FileModel> images) {
        this.images = images;
    }

    public List<FileModel> getFiles() {
        return files;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<NoteHas> getNote_has_label() {
        return note_has_label;
    }

    public void setNote_has_label(List<NoteHas> note_has_label) {
        this.note_has_label = note_has_label;
    }

    public List<NoteHas> getNote_has_user() {
        return note_has_user;
    }

    public void setNote_has_user(List<NoteHas> note_has_user) {
        this.note_has_user = note_has_user;
    }
}
