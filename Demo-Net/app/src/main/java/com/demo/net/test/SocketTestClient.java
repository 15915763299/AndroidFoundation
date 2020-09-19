package com.demo.net.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 11:01
 * description : Socket通讯基本原理展示
 *
 * 注意，四次挥手的最后会有一个等待时间，如果Socket连接太频繁，可能会连接不上
 */
public class SocketTestClient {

    public static void main(String[] args) throws IOException {
        // 1、创建客户端Socket，指定服务器IP、端口
        Socket socket = new Socket("127.0.0.1", SocketTestServer.PORT);

        // 2、获取输出流，发送信息
        OutputStream os = socket.getOutputStream();
        // 输出流包装成打印流（字符流）
        PrintWriter pw = new PrintWriter(os);
        pw.write("hello ~");
        // 3、触发发送（IO流、TCP握手等，都会有一个缓冲队列，
        // 信息不回立即发送，如果需要立即发送，需要主动触发）
        pw.flush();

        // 对应的关闭输出流
        socket.shutdownOutput();
        socket.close();
    }

}
