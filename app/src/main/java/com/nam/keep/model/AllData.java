package com.nam.keep.model;

import java.util.List;

public class AllData {
    private List<User> users;
    private List<Note> notes;
    private List<FileModel> images;
    private List<FileModel> files;
    private List<Label> labels;
    private List<NoteHas> noteHasLabel;
    private List<NoteHas> noteHasUser;


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

    public List<NoteHas> getNoteHasLabel() {
        return noteHasLabel;
    }

    public void setNoteHasLabel(List<NoteHas> noteHasLabel) {
        this.noteHasLabel = noteHasLabel;
    }

    public List<NoteHas> getNoteHasUser() {
        return noteHasUser;
    }

    public void setNoteHasUser(List<NoteHas> noteHasUser) {
        this.noteHasUser = noteHasUser;
    }
}
