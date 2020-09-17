package com.demo.ipc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 14:11
 * description :
 */
public class Library implements Parcelable, Serializable {

    private static final long serialVersionUID = 519067123721295733L;

    private String name;
    private List<Book> books;

    public Library(String name) {
        this.name = name;
    }

    private Library(Parcel in) {
        name = in.readString();
        books = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<Library> CREATOR = new Creator<Library>() {
        @Override
        public Library createFromParcel(Parcel in) {
            return new Library(in);
        }

        @Override
        public Library[] newArray(int size) {
            return new Library[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(books);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if (books == null) {
            books = new ArrayList<>();
        }
        books.add(book);
    }

}
