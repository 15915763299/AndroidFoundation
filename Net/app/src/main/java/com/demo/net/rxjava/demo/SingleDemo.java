package com.demo.net.rxjava.demo;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 21:15
 * description : Single的SingleObserver中只有onSuccess、onError，并没有onComplete。
 */
public class SingleDemo {

    public static void main(String... args) {
        final Disposable test = Single
                .create(emitter -> emitter.onSuccess("test"))
                .subscribe(System.out::println);

        final Disposable test1 = Single
                .create(emitter -> emitter.onSuccess("test1"))
                .subscribe(System.out::println, Throwable::printStackTrace);

        final Disposable test2 = Single
                .create(emitter -> emitter.onError(new RuntimeException("出错误了")))
                .subscribe(System.out::println, Throwable::printStackTrace);

        final Disposable test3 = Single
                .create(emitter -> emitter.onSuccess("test3"))
                .subscribe(s -> System.out.println("s: " + s), Throwable::printStackTrace);
    }

}
