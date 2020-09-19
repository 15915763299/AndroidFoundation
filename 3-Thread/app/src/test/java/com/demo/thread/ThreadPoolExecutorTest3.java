package com.demo.thread;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 尉迟涛
 * create time : 2020/9/19 11:38
 * description :
 */
public class ThreadPoolExecutorTest3 {

    @Test
    public void test() {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                60,
                TimeUnit.SECONDS,
                queue
        );

        executor.execute(() -> {
            System.out.println("任务 1");
            while (true) {
            }

//            try {
//                Thread.sleep(1_000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        });
        executor.execute(() -> {
            System.out.println("任务 2");
        });
//        executor.execute(() -> {
//            System.out.println("任务 3");
//        });

        // 执行以上代码，任务2会一直阻塞在队列中
    }
}
