package com.demo.net.rxjava.demo;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;

public class RepeatUntilDemo {

    public static void main(String... args) {
        repeatUntil();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void repeatUntil() {
        final long start = System.currentTimeMillis();
        final Disposable disposable = Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .take(5)
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        System.out.println("getAsBoolean: " + System.currentTimeMillis());
                        return System.currentTimeMillis() - start > 5000;
                    }
                })
                .subscribe(
                        aLong -> System.out.println("aLong: " + aLong),
                        Throwable::printStackTrace
                );
    }
}
