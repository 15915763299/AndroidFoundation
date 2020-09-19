package com.demo.thread.analysis;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 尉迟涛
 * create time : 2020/2/15 22:18
 * description : 并行计算数组的和（平均拆分后多线程计算）
 */
public class ConcurrentCalculator {

    private ExecutorService exec;
    private int cpuCoreNumber;
    private List<Future<Long>> tasks = new ArrayList<>();

    static class SumCalculator implements Callable<Long> {
        private int[] numbers;
        private int start;
        private int end;

        SumCalculator(final int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() {
            long sum = 0L;
            for (int i = start; i < end; i++) {
                sum += numbers[i];
            }
            return sum;
        }
    }

    public ConcurrentCalculator() {
        cpuCoreNumber = Runtime.getRuntime().availableProcessors();
        exec = new ThreadPoolExecutor(
                cpuCoreNumber,
                cpuCoreNumber,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().build()
        );
    }

    public Long sum(final int[] numbers) {
        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
        for (int i = 0; i < cpuCoreNumber; i++) {
            int increment = numbers.length / cpuCoreNumber + 1;
            int start = increment * i;
            int end = increment * i + increment;
            if (end > numbers.length) {
                end = numbers.length;
            }
            SumCalculator subCalc = new SumCalculator(numbers, start, end);
            FutureTask<Long> task = new FutureTask<>(subCalc);
            tasks.add(task);
            if (!exec.isShutdown()) {
                exec.submit(task);
            }
        }
        return getResult();
    }

    /**
     * 迭代每个只任务，获得部分和，相加返回
     */
    private Long getResult() {
        Long result = 0L;
        for (Future<Long> task : tasks) {
            try {
                // 如果计算未完成则阻塞
                Long subSum = task.get();
                result += subSum;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void close() {
        exec.shutdown();
    }
}
