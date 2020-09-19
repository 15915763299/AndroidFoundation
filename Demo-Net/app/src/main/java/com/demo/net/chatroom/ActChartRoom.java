package com.demo.net.chatroom;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.net.IMsgDispatcher;
import com.demo.net.IOnReceiveListener;
import com.demo.net.R;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 18:39
 * description : 使用binder与后台service通讯，设置回调监听，service独立进程
 */
public class ActChartRoom extends AppCompatActivity {

    private static final String TAG = ActChartRoom.class.getSimpleName();
    private IMsgDispatcher msgDispatcher;
    private Intent intent;

    /**
     * Binder回调
     */
    private IOnReceiveListener.Stub listener = new IOnReceiveListener.Stub() {
        @Override
        public void onHeartBeat(String msg) throws RemoteException {
            // binder 线程
            Log.e(TAG, Thread.currentThread().getName());
            Message.obtain(handler, 0, msg).sendToTarget();
        }

        @Override
        public void onReceiveMsg(String msg) throws RemoteException {
            Message.obtain(handler, 1, msg).sendToTarget();
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            msgDispatcher = IMsgDispatcher.Stub.asInterface(service);
            try {
                // 注册远程listener
                msgDispatcher.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            msgDispatcher = null;
        }
    };

    private StringBuilder sb;
    private TextView tx;
    private ReceiveHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chart_room);

        EditText edt = findViewById(R.id.edt);
        Button btn = findViewById(R.id.btn);
        tx = findViewById(R.id.tx);

        btn.setOnClickListener((View v) -> {
            String msg = edt.getText().toString();
            if (msg.length() > 0) {
                edt.setText("");
                try {
                    msgDispatcher.sendMsg(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        intent = new Intent(this, SerConnector.class);
        handler = new ReceiveHandler(this);
    }

    private void setText(String info) {
        if (sb == null || sb.length() > 1000) {
            sb = new StringBuilder();
        }
        sb.append(info).append("\n");
        tx.setText(sb.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    /**
     * binder线程回调
     */
    private static class ReceiveHandler extends Handler {
        WeakReference<ActChartRoom> wf;

        ReceiveHandler(ActChartRoom act) {
            this.wf = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            if (wf != null && wf.get() != null) {
                ActChartRoom act = wf.get();
                switch (msg.what) {
                    case 0:
                        act.setText((String) msg.obj);
                        break;
                    case 1:
                        act.setText((String) msg.obj);
                        break;
                    default:
                        act.tx.setText("未知消息");
                }
            }
        }
    }

}
