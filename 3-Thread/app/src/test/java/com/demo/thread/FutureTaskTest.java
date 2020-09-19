package com.demo.thread;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author 尉涛
 * @date 2020-02-01 11:09
 * FutureTask 特点：
 * 调用 futureTask.get(); 获取结果时，调用线程会阻塞等到 futureTask 执行完毕才返回结果
 * （get内是一个死循环）
 **/
public class FutureTaskTest {

//    Executor executor;
//    FutureTask futureTask;
//    ArrayDeque arrayDeque;
//    Callable callable;

    /**
     * FutureTask 测试
     */
    @Test
    public void test(){
        MyCallableTask myTask = new MyCallableTask("##", "-");
        FutureTask<Object> futureTask = new FutureTask<>(myTask);
        //采用thread来开启多线程，FutureTask继承了Runnable，可以放在线程池中来启动执行
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Thread.sleep(1000);
            System.out.println("future is done ? " + futureTask.isDone());

            // get():获取任务执行结果，如果任务还没完成则会阻塞等待直到任务执行完成。
            // 如果任务被取消则会抛出CancellationException异常，
            // 如果任务执行过程发生异常则会抛出ExecutionException异常，
            // 如果阻塞等待过程中被中断则会抛出InterruptedException异常。
            boolean result = (boolean) futureTask.get();
            System.out.println("result: " + result);
            System.out.println("future is done ? " + futureTask.isDone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * FutureTask 测试
     */
    private static class MyCallableTask implements Callable<Object> {
        private String args1;
        private String args2;

        MyCallableTask(String args1, String args2) {
            this.args1 = args1;
            this.args2 = args2;
        }

        /**
         * 模拟耗时任务
         */
        @Override
        public Object call() throws Exception {
            for (int i = 0; i < 10; i++) {
                System.out.println(args1 + args2 + i);
                Thread.sleep(500);
            }
            return true;
        }
    }
}
