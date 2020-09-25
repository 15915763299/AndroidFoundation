package com.demo.net.netdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 14:51
 * description :
 */
public class UdpTestService {

    static final int PORT = 12347;

    public static void main(String[] args) throws IOException {
        // 创建DatagramSocket，指定一个接口
        DatagramSocket datagramSocket = new DatagramSocket(PORT);
        // 缓冲队列
        byte[] bytes = new byte[1024];

        // 创建DatagramPacket
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        datagramSocket.receive(datagramPacket);
        System.out.println("接收到的数据：" +
                new String(datagramPacket.getData()));
    }

}
