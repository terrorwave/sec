package com.software.application;

import android.os.Parcel;
import android.os.Parcelable;

public class DataModel_v1 implements Parcelable {
    private String name;
    private boolean isSelected;


    public static final Parcelable.Creator<DataModel_v1> CREATOR = new Parcelable.Creator<DataModel_v1>() {
        public DataModel_v1 createFromParcel(Parcel in) {
            return new DataModel_v1(in);
        }

        public DataModel_v1[] newArray(int size) {
            return new DataModel_v1[size];
        }
    };
    public DataModel_v1(Parcel in) {
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte((byte) (this.isSelected ? 1 : 0));
    }
    public DataModel_v1(String name, boolean gender) {
        this.name = name;
        this.isSelected = false; // not selected when create
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
