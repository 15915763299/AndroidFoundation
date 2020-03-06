package com.demo.net.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 21:02
 * description :
 */
public class DoDemo {

    public static void main(String... args) {
        final Disposable subscribe = Observable
                .just("hello")
                .doOnNext(s -> System.out.println("doOnNext: " + s))
                .doAfterNext(s -> System.out.println("doAfterNext: " + s))
                .doOnComplete(() -> System.out.println("doOnComplete: "))
                .doOnSubscribe(disposable -> System.out.println("doOnSubscribe: " + disposable))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate: "))
                .doFinally(() -> System.out.println("doFinally: "))
                .doOnEach((notify) -> System.out.println("doOnEach: "
                        + (notify.isOnNext()
                        ? "onNext" : notify.isOnComplete()
                        ? "onComplete" : "onEror")))
                .doOnLifecycle(disposable -> System.out.println("doOnLifecycle: " + disposable.isDisposed())
                        , () -> System.out.println("doOnLifecycle run"))
                .subscribe(s -> System.out.println("收到消息: " + s));

        //1、doOnSubscribe: io.reactivex.internal.operators.observable.ObservableDoOnEach$DoOnEachObserver@64cee07
        //2、doOnLifecycle: false
        //3、doOnNext: hello
        //4、doOnEach: onNext
        //5、收到消息: hello
        //6、doAfterNext: hello
        //7、doOnComplete:
        //8、doOnEach:
        //9、onComplete
        //10、doFinally:
        //11、doAfterTerminate:
    }
}
