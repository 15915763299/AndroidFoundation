package com.demo.threadandcache.analysis.example2;

import android.util.Log;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:13
 * description : 传输者，中间多一个包装者
 */
class Transmitter {
    private static final String TAG = Transmitter.class.getSimpleName();

    private String packet;
    // 轮流（锁的是当前对象，对于多个线程turns是个单例）
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
