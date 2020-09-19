package com.demo.net.rxjava.demo;


import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RetryDemo {
    private static final String TAG = "RetryDemo";
    private static int count = 0;//重连次数

    public static void main(String... args) {
        retry();
    }

    private static void retry() {
        final Disposable retry = Observable
                .create(emitter -> {
                    for (int i = 0; i < 100; i++) {
                        emitter.onNext(i);
                        if (i == 2) {//模拟网络请求出错
                            emitter.onError(new IOException("retry error"));
                        }
                    }
                    emitter.onComplete();
                })
                // 捕获 IOException，重试
                .retry(throwable -> {
                    if (throwable instanceof IOException && count++ < 4) {
                        System.out.println("重连次数： " + count);
                        return true;
                    }
                    return false;
                })
                .subscribe(
                        integer -> System.out.println("integer: " + integer),
                        // 打印出捕获的异常
                        throwable -> System.out.println(throwable.getMessage())
                );

    }

}
