package com.demo.ipc;

//同一包内也要import
import com.demo.ipc.model.Book;
import com.demo.ipc.IOnNewBookArrivedListener;

interface IBookManager {

     List<Book> getBookList();

     void addBook(in Book book);

     void registerListener(IOnNewBookArrivedListener listener);

     void unregisterListener(IOnNewBookArrivedListener listener);

}
