package com.demo.net.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 15:28
 * description : 3、模拟服务端
 *
 * 直接在main方法中跑起来，Android客户端配置好本机IP与接口
 */
public class ChatService {

    public static final int PORT = 12348;
    public static final String CLOSE_TAG = "Close";
    public static final String HEART_BEAT = "hb";
    public static final String RECEIVED_HEART_BEAT = "rhb";
    private List<Socket> clients = new ArrayList<>();
    private int index = 0;

    public static void main(String[] args) {
        new ChatService().openChatRoom();
    }

    public void openChatRoom() {
        System.out.println("聊天室启动...");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            ExecutorService executor = Executors.newCachedThreadPool();
            System.out.println("聊天室已打开");

            // 不断获取信息
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("欢迎【" + client.getInetAddress() + "】来到聊天室");
                clients.add(client);
                // 获取到的信息交给线程池处理
                executor.execute(new MemberRunnable(client, index++));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clients.size() > 0) {
                    for (Socket client : clients) {
                        if (client != null) {
                            client.close();
                        }
                    }
                }
                clients.clear();
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取Socket信息的任务，并返回信息
     */
    private class MemberRunnable implements Runnable {
        private Socket socket;
        private BufferedReader br;
        private int index;

        MemberRunnable(Socket socket, int index) {
            this.socket = socket;
            this.index = index;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                sendMsg("你是第" + index + "位来到聊天室的成员");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMsg(String msg) {
            try {
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
                pw.write(msg);
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String data;
            while (!socket.isClosed()) {
                try {
                    if ((data = br.readLine()) != null) {
                        System.out.println(data);
                        if (CLOSE_TAG.equals(data)) {
                            socket.close();
                            break;
                        } else if (HEART_BEAT.equals(data)) {
                            sendMsg(RECEIVED_HEART_BEAT);
                        } else {
                            sendMsg("服务端收到消息：" + data);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println("disconnect socket: " + index);
            clients.set(index, null);
        }
    }
}
