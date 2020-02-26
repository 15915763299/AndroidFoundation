package com.demo.net.test;

import java.net.InetAddress;
import java.util.Arrays;

/**
 * @author 尉迟涛
 * create time : 2020/2/26 10:21
 * description :
 */
public class InetAddressTest {

    public static void main(String[] args) throws Exception{
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("计算机：" + address.getHostName());
        System.out.println("IP地址：" + address.getHostAddress());

        byte[] bytes = address.getAddress();
        System.out.println("IP bytes：" + Arrays.toString(bytes));
        for (byte b:bytes){
            System.out.println(Integer.toBinaryString((int)b));
        }
        System.out.println(address);

        System.out.println("-------------------------");
        System.out.println(Integer.toBinaryString(192));
        System.out.println(Integer.toBinaryString(-64));
    }

}
