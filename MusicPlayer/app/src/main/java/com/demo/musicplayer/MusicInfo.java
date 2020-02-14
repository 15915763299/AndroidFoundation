package com.demo.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 尉迟涛
 * create time : 2020/2/14 14:40
 * description :
 */
public class MusicInfo implements Parcelable {
    private String fileName;
    private String author;
    private String title;

    public MusicInfo(String fileName, String author, String title) {
        this.fileName = fileName;
        this.author = author;
        this.title = title;
    }

    protected MusicInfo(Parcel in) {
        fileName = in.readString();
        author = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(author);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
