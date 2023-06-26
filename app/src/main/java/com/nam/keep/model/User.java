package com.nam.keep.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MultipartBody;

public class User implements Parcelable {
    private long id;
    private String name;
    private String avatar;
    private String email;
    private String password;
    private int isSync;
    private int isChecked;

    // api
    private String password_confirmation;
    private String email_verified_at;
    private String remember_token;
    private String created_at;
    private String updated_at;
    private String device_name;
    private long tokenable_id;
    private String token;
    MultipartBody.Part avatarFile;

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        avatar = in.readString();
        isChecked = in.readInt();
    }

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

    public User(long id, String name, String avatar, String email, String password) {
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

    public User(String name, String avatar, String email, String password, String updated_at, int isSync) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
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

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public long getTokenable_id() {
        return tokenable_id;
    }

    public void setTokenable_id(long tokenable_id) {
        this.tokenable_id = tokenable_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MultipartBody.Part getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(MultipartBody.Part avatarFile) {
        this.avatarFile = avatarFile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(avatar);
        parcel.writeInt(isChecked);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
