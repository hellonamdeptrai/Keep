package com.nam.keep.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Label implements Parcelable {
    private long id;
    private String title;
    private int isChecked;

    public Label() {
    }

    public Label(long id) {
        this.id = id;
    }

    public Label(String title) {
        this.title = title;
    }

    protected Label(Parcel in) {
        id = in.readLong();
        title = in.readString();
        isChecked = in.readInt();
    }

    public Label(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Label(long id, String title, int isChecked) {
        this.id = id;
        this.title = title;
        this.isChecked = isChecked;
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

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeInt(isChecked);
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };
}
