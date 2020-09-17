
package com.demo.ipc;

import com.demo.ipc.model.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
