package com.demo.threadandcache.analysis;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 尉迟涛
 * create time : 2020/2/22 16:39
 * description :
 */
public class LockReadWriteAnalysis {

    public static void main(String[] args) {
//        final S1 s1 = new S1(0);
//        run(s1);
        //read time: 1725
        //write time: 1665

        final S2 s2 = new S2(0);
        run(s2);
        //read time: 63
        //write time: 63
    }

    private static void run(Statistics statistics) {
        for (int i = 0; i < 3; i++) {
            WriteThread writeThread = new WriteThread(statistics);
            for (int j = 0; j < 10; j++) {
                ReadThread readThread = new ReadThread(statistics);
                readThread.start();
            }
            ms(100);
            writeThread.start();
        }
    }

    private static void ms(int seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }

    private static interface Statistics {
        int getTotalAmount();

        void setTotalAmount(int totalAmount);
    }


    /**
     * synchronized 同步锁实现
     */
    private static class S1 implements Statistics {
        private int totalAmount;

        public S1(int totalAmount) {
            this.totalAmount = totalAmount;
        }

        public synchronized int getTotalAmount() {
            ms(5);
            return totalAmount;
        }

        public synchronized void setTotalAmount(int totalAmount) {
            ms(5);
            this.totalAmount = totalAmount;
        }
    }

    /**
     * 读写锁实现
     */
    private static class S2 implements Statistics {
        private ReentrantReadWriteLock lock;
        private int totalAmount;

        public S2(int totalAmount) {
            lock = new ReentrantReadWriteLock();
            this.totalAmount = totalAmount;
        }

        public int getTotalAmount() {
            lock.readLock().lock();
            try {
                return totalAmount;
            } finally {
                lock.readLock().unlock();
            }
        }

        public synchronized void setTotalAmount(int totalAmount) {
            lock.writeLock().lock();
            try {
                this.totalAmount = totalAmount;
            } finally {
                lock.writeLock().unlock();
            }
        }
    }


    private static class ReadThread extends Thread {
        private Statistics statistics;

        public ReadThread(Statistics statistics) {
            this.statistics = statistics;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                ms(5);
                statistics.getTotalAmount();
            }
            System.out.println("read time: " + (System.currentTimeMillis() - start));
        }
    }


    private static class WriteThread extends Thread {
        private Statistics statistics;

        public WriteThread(Statistics statistics) {
            this.statistics = statistics;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                ms(5);
                statistics.setTotalAmount(random.nextInt(100));
            }
            System.out.println("write time: " + (System.currentTimeMillis() - start));
        }
    }
}
