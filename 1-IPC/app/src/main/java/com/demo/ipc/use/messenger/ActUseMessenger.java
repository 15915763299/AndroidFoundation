package com.demo.ipc.use.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.ipc.R;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 18:37
 * description : messager信使（串行执行），底层由AIDL实现
 */
public class ActUseMessenger extends AppCompatActivity {

    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;
    private static final String TAG = ActUseMessenger.class.getSimpleName();

    private static class ClientHandler extends Handler {

        private WeakReference<ActUseMessenger> wr;

        ClientHandler(ActUseMessenger act) {
            this.wr = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_FROM_SERVICE) {
                String reply = msg.getData().getString("reply");
                if (wr != null && wr.get() != null) {
                    wr.get().tx.setText(reply);
                }
                Log.i(TAG, "receive msg from Service:" + reply);
            } else {
                super.handleMessage(msg);
            }
        }
    }

    // Handler 属于 TLS(Thread Local Storage) 变量, 生命周期和 Activity 是不一致的。
    // 所以很有可能我们的Activity的逻辑早就结束了，但是Handler持有Activity的引用导致Activity的资源无法回收。
    // Handler 持有 Looper，Lopper被 ThreadLocal 持有

    // IMessenger 的实现是 Handler 的内部类，它实现了send方法，给初始化的Handler发送信息

    private Messenger clientMessenger = new Messenger(new ClientHandler(this));
    private TextView tx;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger serviceMessenger = new Messenger(service);
            Log.d(TAG, "bind service");

            //obj不能使用非系统声明的Serializable实体，所以使用Bundle
            Message msg = Message.obtain(null, MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client.");
            msg.setData(data);
            msg.replyTo = clientMessenger;
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_messenger);

        tx = findViewById(R.id.tx);
        Intent intent = new Intent("android.intent.action.USE_MESSENGER");
        intent.setPackage(getPackageName());
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
