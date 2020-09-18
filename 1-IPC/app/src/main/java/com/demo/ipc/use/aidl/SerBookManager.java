package com.demo.ipc.use.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.demo.ipc.IBookManager;
import com.demo.ipc.IOnNewBookArrivedListener;
import com.demo.ipc.model.Book;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 11:57
 * description :
 */
public class SerBookManager extends Service {

    private static final String TAG = SerBookManager.class.getSimpleName();
    private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);
    /**
     * thread-safe
     */
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> listenerList = new RemoteCallbackList<>();
    private ThreadPoolExecutor threadPoolExecutor;

    private Binder binder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(3000);
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            //onNewBookArrived(book);
            threadPoolExecutor.execute(new AddBookThread(SerBookManager.this, book));
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listenerList.register(listener);
            final int n = listenerList.beginBroadcast();
            listenerList.finishBroadcast();
            Log.d(TAG, "registerListener, current size:" + n);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            boolean success = listenerList.unregister(listener);
            if (success) {
                Log.d(TAG, "unregister success.");
            } else {
                Log.d(TAG, "not found, can not unregister.");
            }
            final int n = listenerList.beginBroadcast();
            listenerList.finishBroadcast();
            Log.d(TAG, "unregisterListener, current size:" + n);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        createThreadPool();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 检测当前进程自定义权限
        int check = checkCallingOrSelfPermission("com.ipc.permission.ACCESS_BOOK_SERVICE");
        Log.d(TAG, "onBind check=" + check);
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return binder;
    }

    @Override
    public void onDestroy() {
        isServiceDestroyed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        bookList.add(book);
        final int n = listenerList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            IOnNewBookArrivedListener l = listenerList.getBroadcastItem(i);
            if (l != null) {
                l.onNewBookArrived(book);
            }
        }
        listenerList.finishBroadcast();
    }

    private void createThreadPool() {
        // 使用ThreadFactoryBuilder快速建立ThreadFactory
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        threadPoolExecutor = new ThreadPoolExecutor(
                //设置为1的时候线程串行
                5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    private static class AddBookThread extends Thread {
        private WeakReference<SerBookManager> wr;
        private Book book;

        AddBookThread(SerBookManager ser, Book book) {
            this.wr = new WeakReference<>(ser);
            this.book = book;
        }

        @Override
        public void run() {
            if (wr != null && wr.get() != null) {
                SerBookManager ser = wr.get();
                if (!wr.get().isServiceDestroyed.get()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        ser.onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
