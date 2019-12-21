package com.demo.ipc.use.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.ipc.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

/**
 * @author 尉迟涛
 * create time : 2019/11/15 23:05
 * description :
 */
public class ActUseSocket extends AppCompatActivity implements View.OnClickListener {

    private static final int SEND_NEW_MSG = 0;
    private static final int RECEIVE_NEW_MSG = 1;
    private static final int SOCKET_CONNECTED = 2;

    private static class MainThreadHandler extends Handler {

        private WeakReference<ActUseSocket> wr;

        public MainThreadHandler(ActUseSocket act) {
            this.wr = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (wr == null || wr.get() == null) {
                return;
            }
            ActUseSocket act = wr.get();

            switch (msg.what) {
                case SEND_NEW_MSG:
                    act.edt.setText("");
                    break;
                case RECEIVE_NEW_MSG:
                    String text = act.tx.getText().toString();
                    act.tx.setText(text + "\n" + msg.obj);
                    break;
                case SOCKET_CONNECTED:
                    act.btn_send.setEnabled(true);
                    break;
                default:
            }

        }
    }


    private Handler handler = new MainThreadHandler(this);
    private Button btn_send;
    private EditText edt;
    private TextView tx;
    private PrintWriter printWriter;
    private Socket client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_use_socket);

        edt = findViewById(R.id.edt);
        tx = findViewById(R.id.tx);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        btn_send.setEnabled(false);
        findViewById(R.id.btn_clear).setOnClickListener(this);

        Intent intent = new Intent(this, SerUseSocketRemote.class);
        startService(intent);

        new Thread(new ClientRunnable()).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                final String msg = edt.getText().toString();
                if (!TextUtils.isEmpty(msg) && printWriter != null) {
                    new Thread(() -> {
                        printWriter.println(msg);
                        handler.obtainMessage(SEND_NEW_MSG).sendToTarget();
                    }).start();
                }
                break;
            case R.id.btn_clear:
                tx.setText("");
                break;
            default:
        }
    }

    private class ClientRunnable implements Runnable {
        @Override
        public void run() {
            while (client == null) {
                try {
                    client = new Socket("localhost", 8688);
                    printWriter = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream())), true);
                    handler.sendEmptyMessage(SOCKET_CONNECTED);
                    System.out.println("connect server success");
                } catch (IOException e) {
                    SystemClock.sleep(1000);
                    System.out.println("connect tcp server failed, retry...");
                }
            }
            loop();
        }

        private void loop() {
            try {
                // 接收服务器端的消息
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (!ActUseSocket.this.isFinishing()) {
                    String msg = br.readLine();
                    System.out.println("receive :" + msg);
                    if (msg != null) {
                        handler.obtainMessage(RECEIVE_NEW_MSG, msg).sendToTarget();
                    }
                }
                System.out.println("quit...");
                close(printWriter);
                close(br);
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void close(Closeable closeable) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (client != null) {
            try {
                client.shutdownInput();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

}
