package com.demo.net.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 15:12
 * description :
 */
public class UdpTestClient {

    public static void main(String[] args) throws IOException {
        // 创建DatagramSocket
        DatagramSocket datagramSocket = new DatagramSocket();

        // 创建DatagramPacket，然后send
        String msg = "hi~~";
        DatagramPacket datagramPacket = new DatagramPacket(
                msg.getBytes(),
                msg.getBytes().length,
                InetAddress.getLocalHost(),
                UdpTestService.PORT
        );

        datagramSocket.send(datagramPacket);
    }

}
