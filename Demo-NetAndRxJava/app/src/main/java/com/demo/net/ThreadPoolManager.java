package com.demo.net;

import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * App 线程池单例
 */
public class ThreadPoolManager {

    private static final String TAG = ThreadPoolManager.class.getSimpleName();
    private static final int NUMBER_OF_CORES = Math.max(3, Math.min(Runtime.getRuntime().availableProcessors() - 1, 6));
    private static final int FIXED_MAX_QUEUE_SIZE = NUMBER_OF_CORES * 10;

    private static class SingletonHolder {
        private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    }

    private final ExecutorService ioFixedPool;

    public static ThreadPoolManager get() {
        return SingletonHolder.INSTANCE;
    }

    private ThreadPoolManager() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

        ioFixedPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES, NUMBER_OF_CORES * 2 + 1,
                30L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(FIXED_MAX_QUEUE_SIZE),
                namedThreadFactory,
                new IgnorePolicy()
        );
    }

    public void execute(Runnable runnable) {
        try {
            if (runnable != null) {
                ioFixedPool.execute(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        ioFixedPool.shutdown();
    }

    public static class IgnorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Log.v(TAG, String.format("runnable %s rejected by %s", r.toString(), executor.toString()));
        }
    }
}
