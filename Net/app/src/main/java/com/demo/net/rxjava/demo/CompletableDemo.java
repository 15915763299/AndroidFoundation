package com.demo.net.rxjava.demo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 18:47
 * description : Completable 能够发射0或n个数据，并以成功或错误事件终止。
 */
public class CompletableDemo {

    public static void main(String... args) {
//        Completable.fromAction(() -> System.out.println("hello world!"))
//                .subscribe();

        // 在这里emitter.onComplete()执行完之后，表明Completable已经完全执行完毕，
        // 接下来是执行andThen里的操作
        // Completable 只有 onComplete 和 onError 事件
        final Disposable disposable = Completable.create(
                emitter -> {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                })
                .andThen(Observable.range(1, 10))
                .subscribe(System.out::println);
    }

}
