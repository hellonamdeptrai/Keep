package com.nam.keep.model;

public class FileModel {
    private String name;
    private String path;
    private long idNote;

    // api
    private String note_id;
    private String created_at;
    private String updated_at;

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

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
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
}
