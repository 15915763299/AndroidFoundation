package com.demo.threadandcache.analysis.example;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 17:13
 * description : 传输者
 * <p>
 * 为何要把这两个方法放在同一个类中呢
 * 是因为要通过一个对象来控制整个传输流程的顺序的正确性
 *
 *  wait会释放锁，等到其他线程调用notify方法时再继续运行。
 *  但是一定要持有锁，才能wait。
 *
 *  Transmitter对象作为锁
 */
class Transmitter {
    private String packet;

    // 是否正在传输
    private boolean transferring = false;

    /**
     * 发送方法，只在 sender 线程内调用
     *
     * @param packet 信息
     */
    synchronized void send(String packet) {
        System.out.println("sender 获取了锁");
        while (transferring) {
            System.out.println("上一个正在传输中，等待");
            try {
                wait();// 这个wait阻塞了发送线程，直到接收线程执行完接收后，会将它notify
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("传输完成，发送新的传输数据，继续传输");
        transferring = true;
        this.packet = packet;
        notifyAll();
    }

    /**
     * 接收方法，只在 receiver 线程内调用
     *
     * @return 信息
     */
    synchronized String receive() {
        System.out.println("receiver 获取了锁");
        while (!transferring) {
            System.out.println("上一个传输已完成，等待");
            try {
                wait(); // 这个wait阻塞了接收线程，直到发送线程执行完发送后，会将它notify
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("获取到下一个传输，执行获取程序");
        transferring = false;
        notifyAll();
        return packet;
    }
}
