package com.nam.keep.model;

public class User {
    private long id;
    private String name;
    private byte[] avatar;
    private String email;
    private String password;
    private int isSync;

    // api
    private String password_confirmation;
    private String email_verified_at;
    private String remember_token;
    private String created_at;
    private String updated_at;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(long id, String name, byte[] avatar, String email, String password) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, String password_confirmation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public User(String name, byte[] avatar, String email, String password, String updated_at, int isSync) {
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.password = password;
        this.updated_at = updated_at;
        this.isSync = isSync;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
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
}
