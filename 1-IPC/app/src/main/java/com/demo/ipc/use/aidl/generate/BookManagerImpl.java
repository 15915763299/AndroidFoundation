package com.demo.ipc.use.aidl.generate;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.ipc.model.Book;

import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/12/10 0:04
 * description : 手写自动生成代码
 */
public class BookManagerImpl extends Binder implements IBookManager {

    private static final java.lang.String DESCRIPTOR = "com.demo.ipc.use.aidl.generate.IBookManager";
    private static final int TRANSACTION_getBookList = (IBinder.FIRST_CALL_TRANSACTION);
    private static final int TRANSACTION_addBook = (IBinder.FIRST_CALL_TRANSACTION + 1);

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin instanceof com.demo.ipc.IBookManager) {
            return ((IBookManager) iin);
        }
        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        //TODO 待实现
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        //TODO 待实现
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                if (reply != null) {
                    reply.writeString(DESCRIPTOR);
                }
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                if (reply != null) {
                    reply.writeNoException();
                    reply.writeTypedList(result);
                }
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if ((0 != data.readInt())) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.addBook(book);
                if (reply != null) {
                    reply.writeNoException();
                }
                return true;
            }
            default: {
                return super.onTransact(code, data, reply, flags);
            }
        }
    }


    private static class Proxy implements IBookManager {
        private IBinder mRemote;

        Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();

            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(com.demo.ipc.model.Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null)) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }
    }
}
