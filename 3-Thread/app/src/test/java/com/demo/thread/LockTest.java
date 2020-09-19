package com.demo.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 14:17
 * description :
 * <p>
 * 对Lock的说明：https://www.cnblogs.com/wuhan729/p/8601108.html
 *
 * lock.newCondition();可以获取 condition 对象，可以实现 wait 和 notify
 * 一个lock可以生成多个condition实例，可以根据业务分多个condition，condition的唤醒，少用singalall
 */
public class LockTest {

    /**
     * lock.lock();的使用方式
     */
    @Test
    public void testLock() throws InterruptedException {
        final LockMethodTest lockMethodTest = new LockMethodTest();
        Thread t0 = new Thread() {
            @Override
            public void run() {
                lockMethodTest.insert(Thread.currentThread());
            }
        };
        Thread t1 = new Thread() {
            @Override
            public void run() {
                lockMethodTest.insert(Thread.currentThread());
            }
        };
        t0.start();
        t1.start();
        t0.join();
        t1.join();
    }

    private static class LockMethodTest {
        private ArrayList<Integer> arrayList = new ArrayList<>();
        private Lock lock = new ReentrantLock();

        void insert(Thread thread) {
            lock.lock();
            try {
                System.out.println(thread.getName() + "得到了锁");
                for (int i = 0; i < 5; i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(thread.getName() + "释放了锁");
            }
        }
    }

    /**
     * lock.tryLock();的使用方式
     */
    @Test
    public void tryLockTest() throws InterruptedException {
        final TryLockTest tryLockTest = new TryLockTest();
        Thread t2 = new Thread() {
            @Override
            public void run() {
                tryLockTest.insert(Thread.currentThread());
            }
        };
        Thread t3 = new Thread() {
            @Override
            public void run() {
                tryLockTest.insert(Thread.currentThread());
            }
        };
        t2.start();
        t3.start();
        t2.join();
        t3.join();
    }

    private static class TryLockTest {
        private ArrayList<Integer> arrayList = new ArrayList<>();
        private Lock lock = new ReentrantLock();

        void insert(Thread thread) {
            boolean result = false;
            try {
                //会等待的 tryLock
                result = lock.tryLock(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //TODO 瞬间返回的 tryLock，会报获取锁失败
//            if (lock.tryLock()) {
            if (result) {
                try {
                    System.out.println(thread.getName() + "得到了锁");
                    for (int i = 0; i < 5; i++) {
                        arrayList.add(i);
                    }
                } finally {
                    System.out.println(thread.getName() + "释放了锁");
                    lock.unlock();
                }
            } else {
                System.out.println(thread.getName() + "获取锁失败");
            }
        }
    }


    /**
     * lock.lockInterruptibly();的使用方式
     */
    @Test
    public void lockInterruptiblyTest() throws InterruptedException {
        final LockInterruptiblyTest lockInterruptiblyTest = new LockInterruptiblyTest();
        Thread t4 = new Thread() {
            @Override
            public void run() {
                lockInterruptiblyTest.insert(Thread.currentThread());
            }
        };
        Thread t5 = new Thread() {
            @Override
            public void run() {
                lockInterruptiblyTest.insert(Thread.currentThread());
            }
        };
        t4.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t5.start();

        // 等待一下，然后打断t5
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t5.interrupt();

        // 对于读写锁 ReentrantReadWriteLock，这里就不多写示例了
        // ReentrantReadWriteLock 中 readLock() 和 writeLock() 用来获取读锁和写锁。
    }

    private static class LockInterruptiblyTest {
        private Lock lock = new ReentrantLock();

        void insert(Thread thread) {
            try {
                lock.lockInterruptibly();
                try {
                    System.out.println(thread.getName() + "得到了锁");
                    long startTime = System.currentTimeMillis();
                    for (; ; ) {
                        if (System.currentTimeMillis() - startTime >= Integer.MAX_VALUE)
                            break;
                    }
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + "执行finally");
                    System.out.println(thread.getName() + "释放了锁");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
