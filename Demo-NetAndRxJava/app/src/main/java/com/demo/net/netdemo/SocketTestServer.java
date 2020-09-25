package com.demo.net.netdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 10:32
 * description : Socket通讯基本原理展示
 */
public class SocketTestServer {

    static final int PORT = 12346;

    public static void main(String[] args) throws IOException {
        // 1、创建一个服务端的ServerSocket，制定一个端口，监听此端口
        ServerSocket serverSocket = new ServerSocket(PORT);

        // 2、调用accept方法，等待客户端连接（这个方法会产生阻塞）
        System.out.println("before accept");
        Socket socket = serverSocket.accept();
        System.out.println("after accept");

        // 3、获取InputStream、创建OutputStream
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bf = new BufferedReader(isr);

        String data;
        while ((data = bf.readLine()) != null){
            System.out.println("服务端接收到信息：" + data);
        }

        // 收到一条信息我们就关闭了，只是一个Demo，
        // 如果自己写一个Socket框架，需要自行拟定一些协议，比如断开标记、何时重连
        // 刚才打开了输入流，要对应关闭
        socket.shutdownInput();
        // 这里会主动把isr、bf关闭
        socket.close();
    }

}
