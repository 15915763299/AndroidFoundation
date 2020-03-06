package com.demo.net.rxjava.demo;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 18:59
 * description :
 */
public class MaybeDemo {

    public static void main(String... args) {

        // Maybe创建之后，MaybeEmitter 和 SingleEmitter 一样并没有onNext()方法，
        // 同样需要通过onSuccess()方法来发射数据。
        final Disposable testA = Maybe.<String>create(emitter -> emitter.onSuccess("testA"))
                .subscribe(s -> System.out.println("s: " + s));

        // Maybe也只能发射0或者1个数据，即使发射多个数据，后面发射的数据也不会处理。
        // 并且onComplete后面的不会再发射
        final Disposable testB = Maybe.create(emitter -> {
            emitter.onComplete();
            emitter.onSuccess("test1");
            emitter.onSuccess("test2");
        }).subscribe(System.out::println);
    }

}
