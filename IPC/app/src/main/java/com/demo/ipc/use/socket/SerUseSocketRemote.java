package com.demo.ipc.use.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 尉迟涛
 * create time : 2019/11/16 0:24
 * description :
 */
public class SerUseSocketRemote extends Service {

    private boolean isServiceDestroyed = false;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServerRunnable()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ServerRunnable implements Runnable {

        private ServerSocket serverSocket;

        @SuppressWarnings("resource")
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                System.err.println("establish tcp server failed, port:8688");
                e.printStackTrace();
                return;
            }
            loop();
        }

        private void loop() {
            while (!isServiceDestroyed) {
                try {
                    // 接受客户端请求
                    final Socket client = serverSocket.accept();
                    System.out.println("accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void responseClient(Socket client) throws IOException {
            // 用于接收客户端消息
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            // 用于向客户端发送消息
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())), true);
            out.println("服务已连接");
            while (!isServiceDestroyed) {
                String str = in.readLine();
                System.out.println("msg from client:" + str);
                if (str == null) {
                    break;
                }
                out.println("接收到信息：" + str);
            }
            System.out.println("client quit.");
            // 关闭流
            close(out);
            close(in);
            client.close();
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

}
