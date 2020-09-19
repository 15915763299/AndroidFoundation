package com.demo.thread.analysis.example2;

import android.util.Log;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:13
 * description : 传输者，中间多一个包装者
 */
public class Transmitter2 {
    private static final String TAG = Transmitter2.class.getSimpleName();

    private String packet;
    // 轮流（锁的是当前对象，同一时间只有一个线程获取锁，
    // 同一时间只有一个线程会写，不会产生ABA问题，所以可以用volatile）
    private volatile int turns = 0;

    synchronized void send(String packet) {
        while (turns != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.e(TAG, "Thread interrupted", e);
            }
        }

        turns = 1;
        this.packet = packet;
        notifyAll();
    }

    synchronized String pack(Pack pack) {
        while (turns != 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.e(TAG, "Thread interrupted", e);
            }
        }

        packet = pack.pack(packet);
        turns = 2;
        notifyAll();
        return packet;
    }

    synchronized String receive() {
        while (turns != 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.e(TAG, "Thread interrupted", e);
            }
        }

        turns = 0;
        notifyAll();
        return packet;
    }
}
