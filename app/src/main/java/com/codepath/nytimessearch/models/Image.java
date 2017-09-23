package com.codepath.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yingbwan on 9/20/2017.
 */

public class Image implements Parcelable {
    String type;
    String subType;
    String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    int height;
    int width;

    public Image() {

    }

    public Image(String type, String subType, String url, int height, int width) {
        this.type = type;
        this.subType = subType;
        this.url = url;
        this.height = height;
        this.width = width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.subType);
        dest.writeString(this.url);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
    }

    protected Image(Parcel in) {
        this.type = in.readString();
        this.subType = in.readString();
        this.url = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
