package com.demo.net.rxjava.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class IntervalDemo {
    private static final String TAG = "IntervalDemo";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", Locale.CHINA);

    public static void main(String... args) {
        interval();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 周期性操作
     */
    private static void interval() {
        // 该例子发送的事件序列特点：延迟2s后发送事件，
        // 每隔1秒产生1个数字（从0开始递增1，无限个） = 每隔1s进行1次操作
        System.out.println(sdf.format(new Date()));
        final Disposable subscribe = Observable
                .interval(2, 1, TimeUnit.SECONDS)
                //可能是onError或者onComplete重复执行，下面注释的语句会报错：UndeliverableException
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d(TAG, "开始采用subscribe连接");
//                    }
//
//                    @Override
//                    public void onNext(Long value) {
//                        Log.d(TAG, "每隔1s进行1次操作");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "对Error事件作出响应");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "对Complete事件作出响应");
//                    }
//                });
                .subscribe(l -> {
                    System.out.println(sdf.format(new Date()));
                    System.out.println(l);
                });

        // 注：interval默认在computation调度器上执行
        // 也可自定义指定线程调度器（第3个参数）：interval(long,TimeUnit,Scheduler)
    }
}
