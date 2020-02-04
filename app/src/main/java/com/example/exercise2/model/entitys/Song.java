package com.example.exercise2.model.entitys;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String mIDSong;

    private String mNameSong;

    private String mNameAuthor;

    private String mTimeSong;

    private String mPathSong;

    private String mImageSong;

    public Song(String mIDSong, String mNameSong, String mNameAuthor, String mTimeSong, String mPathSong, String mImageSong) {
        this.mIDSong = mIDSong;
        this.mNameSong = mNameSong;
        this.mNameAuthor = mNameAuthor;
        this.mTimeSong = mTimeSong;
        this.mPathSong = mPathSong;
        this.mImageSong = mImageSong;
    }

    public Song() {

    }

    protected Song(Parcel in) {
        mIDSong = in.readString();
        mNameSong = in.readString();
        mNameAuthor = in.readString();
        mTimeSong = in.readString();
        mPathSong = in.readString();
        mImageSong = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getmIDSong() {
        return mIDSong;
    }

    public void setmIDSong(String mIDSong) {
        this.mIDSong = mIDSong;
    }

    public String getmNameSong() {
        return mNameSong;
    }

    public void setmNameSong(String mNameSong) {
        this.mNameSong = mNameSong;
    }

    public String getmNameAuthor() {
        return mNameAuthor;
    }

    public void setmNameAuthor(String mNameAuthor) {
        this.mNameAuthor = mNameAuthor;
    }

    public String getmTimeSong() {
        return mTimeSong;
    }

    public void setmTimeSong(String mTimeSong) {
        this.mTimeSong = mTimeSong;
    }

    public String getmPathSong() {
        return mPathSong;
    }

    public void setmPathSong(String mPathSong) {
        this.mPathSong = mPathSong;
    }

    public String getmImageSong() {
        return mImageSong;
    }

    public void setmImageSong(String mImageSong) {
        this.mImageSong = mImageSong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIDSong);
        dest.writeString(mNameSong);
        dest.writeString(mNameAuthor);
        dest.writeString(mTimeSong);
        dest.writeString(mPathSong);
        dest.writeString(mImageSong);
    }

}
