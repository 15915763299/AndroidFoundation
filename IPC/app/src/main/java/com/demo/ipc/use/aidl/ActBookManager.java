package com.demo.ipc.use.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.ipc.App;
import com.demo.ipc.IBookManager;
import com.demo.ipc.IOnNewBookArrivedListener;
import com.demo.ipc.R;
import com.demo.ipc.model.Book;
import com.demo.ipc.use.file.BookAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 10:36
 * description :
 */
public class ActBookManager extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ActBookManager.class.getSimpleName();
    public static final int MSG_NEW_BOOK_ARRIVED = 1;
    private static final int MSG_GET_SUCCESS = 2;
    private int bookCount = 0;

    private static class MainThreadHandler extends Handler {
        private WeakReference<ActBookManager> wr;

        MainThreadHandler(ActBookManager act) {
            this.wr = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_NEW_BOOK_ARRIVED:
                    Toast.makeText(App.getApp(), "远程添加图书成功", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_GET_SUCCESS:
                    if (wr != null && wr.get() != null) {
                        wr.get().adapter.clearAndAddAll((List<Book>) msg.obj);
                        Toast.makeText(App.getApp(), "获取远程图书列表成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
    }

    private IBookManager remoteBookManager;
    private MainThreadHandler handler = new MainThreadHandler(this);

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "Binder died. Thread name:" + Thread.currentThread().getName());
            if (remoteBookManager == null)
                return;
            remoteBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            remoteBookManager = null;
            // TODO:这里重新绑定远程Service
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Toast.makeText(App.getApp(), "connect success", Toast.LENGTH_SHORT).show();

            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            remoteBookManager = bookManager;
            try {
                remoteBookManager.asBinder().linkToDeath(mDeathRecipient, 0);
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Toast.makeText(App.getApp(), "service disconnect", Toast.LENGTH_SHORT).show();
            remoteBookManager = null;
            Log.d(TAG, "Service disconnect. Thread name:" + Thread.currentThread().getName());
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MSG_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    private EditText edt_book;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_book_manager);

//        Intent intent = new Intent("android.intent.action.USE_AIDL");
//        intent.setPackage(getPackageName());
        Intent intent = new Intent(this, SerBookManager.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        edt_book = findViewById(R.id.edt_book);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_get_book_list).setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BookAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (remoteBookManager == null || !remoteBookManager.asBinder().isBinderAlive()) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_add:
                String bookName = edt_book.getText().toString();
                if (bookName.length() > 0) {
                    new Thread(() -> {
                        try {
                            //耗时操作
                            remoteBookManager.addBook(new Book(bookName, ++bookCount));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
                break;
            case R.id.btn_get_book_list:
                new Thread(() -> {
                    try {
                        //耗时操作
                        List<Book> books = remoteBookManager.getBookList();
                        handler.obtainMessage(MSG_GET_SUCCESS, books).sendToTarget();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        if (remoteBookManager != null && remoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "unregister listener:" + mOnNewBookArrivedListener);
                remoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }
}
