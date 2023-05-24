package com.nam.keep.model;

public class FileModel {
    private String name;
    private String path;
    private long idNote;

    public FileModel() {
    }

    public FileModel(String path, long idNote) {
        this.name = name;
        this.path = path;
        this.idNote = idNote;
    }

    public FileModel(String name, String path, long idNote) {
        this.name = name;
        this.path = path;
        this.idNote = idNote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getIdNote() {
        return idNote;
    }

    public void setIdNote(long idNote) {
        this.idNote = idNote;
    }
}
