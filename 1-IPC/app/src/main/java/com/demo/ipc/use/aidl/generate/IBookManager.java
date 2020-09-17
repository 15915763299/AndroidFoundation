package com.demo.ipc.use.aidl.generate;

import android.os.IInterface;

import com.demo.ipc.model.Book;

import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/12/10 0:03
 * description :
 */
public interface IBookManager extends IInterface {

    List<Book> getBookList() throws android.os.RemoteException;

    void addBook(Book book) throws android.os.RemoteException;

}
