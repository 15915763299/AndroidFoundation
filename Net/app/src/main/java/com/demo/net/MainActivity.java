package com.demo.net;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.net.chatroom.ActChartRoom;

/**
 * 针对不同的网络通信层次，Java给我们提供的网络功能有四大类：
 * <p>
 * InetAddress： 用于标识网络上的硬件资源
 * URL：         统一资源定位符，通过URL可以直接读取或者写入网络上的数据
 * Socket和ServerSocket： 使用TCP协议实现网络通信的Socket相关的类
 * Datagram：    使用UDP协议，将数据保存在数据报中，通过网络进行通信
 *
 * 什么是Socket，是一个对 TCP / IP协议进行封装 的编程调用接口（API）
 * 1、即通过Socket，我们才能在Andorid平台上通过 TCP/IP协议进行开发
 * 2、Socket不是一种协议，而是一个编程调用接口（API），属于传输层（主要解决数据如何在网络中传输）
 * 3、成对出现，一对套接字
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, ActChartRoom.class));
                break;
            default:
        }
    }
}
