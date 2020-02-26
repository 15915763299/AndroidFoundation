package com.demo.net.chatroom;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.demo.net.IMsgDispatcher;
import com.demo.net.IOnReceiveListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 18:46
 * description :
 */
public class SerConnector extends Service {

    private static final String TAG = SerConnector.class.getSimpleName();
    /**
     * 心跳频率
     */
    private static final long HEART_BEAT_RATE = 3 * 1000;
    /**
     * 服务器ip地址
     */
    public static final String HOST = "192.168.2.108";
    /**
     * 远程Binder
     */
    private IMsgDispatcher.Stub msgDispatcher = new IMsgDispatcher.Stub() {
        @Override
        public void sendMsg(String msg) throws RemoteException {
            // binder 线程
            Log.e(TAG, Thread.currentThread().getName());
            SerConnector.this.sendMsg(msg);
        }

        @Override
        public void registerListener(IOnReceiveListener listener) throws RemoteException {
            SerConnector.this.listener = listener;
            if (readThread != null) {
                readThread.setListener(listener);
            }
        }
    };
    /**
     * 处理心跳线程
     */
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                //如果发送失败，就重新初始化一个socket
                boolean isSuccess = sendMsg(ChatService.HEART_BEAT);
                if (!isSuccess) {
                    handler.removeCallbacks(heartBeatRunnable);
                    readThread.release();
                    releaseLastSocket(wf);
                    new InitSocketThread().start();
                }
            }
            handler.postDelayed(this, HEART_BEAT_RATE);
        }
    };
    /**
     * 处理心跳Handler
     */
    private Handler handler = new Handler();
    private long sendTime;
    private ReadThread readThread;
    private WeakReference<Socket> wf;
    private IOnReceiveListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return msgDispatcher;
    }

    @Override
    public void onCreate() {
        new InitSocketThread().start();
    }

    /**
     * 初始化Socket线程
     */
    private class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    /**
     * 初始化Socket
     */
    private void initSocket() {
        try {
            Socket socket = new Socket(HOST, ChatService.PORT);
            wf = new WeakReference<>(socket);
            readThread = new ReadThread(socket);
            if (listener != null) {
                readThread.setListener(listener);
            }
            readThread.start();
            handler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收信息线程
     */
    private class ReadThread extends Thread {
        private WeakReference<Socket> wf;
        private boolean isStart = true;
        private IOnReceiveListener listener;

        ReadThread(Socket socket) {
            this.wf = new WeakReference<>(socket);
        }

        public void setListener(IOnReceiveListener listener) {
            this.listener = listener;
        }

        void release() {
            isStart = false;
            releaseLastSocket(wf);
        }

        @Override
        public void run() {
            if (wf == null || wf.get() == null) {
                return;
            }

            Socket socket = wf.get();
            try {
                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[1024 * 4];
                int length;
                while (!socket.isClosed() && !socket.isInputShutdown() &&
                        isStart && ((length = is.read(buffer)) != -1)) {

                    if (length > 0) {
                        String msg = new String(Arrays.copyOf(buffer, length)).trim();
                        Log.d(TAG, msg);
                        if (listener != null) {
                            if (msg.equals(ChatService.RECEIVED_HEART_BEAT)) {
                                listener.onHeartBeat(msg);
                            } else {
                                listener.onReceiveMsg(msg);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送信息给客户端
     */
    public boolean sendMsg(final String msg) {
        if (null == wf || null == wf.get()) {
            return false;
        }
        final Socket socket = wf.get();
        if (!socket.isClosed() && !socket.isOutputShutdown()) {
            new Thread(() -> {
                try {
                    OutputStream os = socket.getOutputStream();
                    String message = msg + "\r\n";
                    os.write(message.getBytes());
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            //每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            sendTime = System.currentTimeMillis();
        } else {
            return false;
        }
        return true;
    }

    private void releaseLastSocket(WeakReference<Socket> wf) {
        try {
            if (null != wf) {
                Socket sk = wf.get();
                if (!sk.isClosed()) {
                    sk.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
