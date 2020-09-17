package com.demo.ipc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 14:12
 * description :
 */
public class Book implements Parcelable, Serializable {

    private static final long serialVersionUID = 519067123721295773L;

    private String name;
    private int id;

    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }

    protected Book(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
