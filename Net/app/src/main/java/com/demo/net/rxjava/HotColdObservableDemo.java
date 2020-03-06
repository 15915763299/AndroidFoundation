package com.demo.net.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * 冷：观察者订阅了，才会开始执行发射数据流的代码
 * （后来的订阅者会从头接收数据，但是并不会全部接收）
 * （多个订阅者执行顺序不定）
 * <p>
 * 热：只接收订阅之后发出的数据流
 * （后来的订阅者从当下开始接收数据）
 * （多个订阅者执行顺序按订阅先后执行）
 * （热的要注意内存泄漏，用完后一定要记得关掉）
 * <p>
 * 冷：Observable 和 Observer 是一对一的关系
 * 有多个Observer的时候，它们各自的事件是独立的
 * <p>
 * 事件类型         作用
 * onNext()        观察者会回调它的onNext()方法
 * onError()        onError事件发送之后，其他事件不会继续发送
 * onComplete()     onComplete事件发送之后，其他事件不会继续发送
 */
public class HotColdObservableDemo {

    public static void main(String... args) {

//        new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                emitter.onNext(aLong);
//            }
//        }
//        ↑变为↓
//        emitter::onNext

        //创建一个"冷"的被观察者
        Observable<Long> cold = Observable
                .create((ObservableEmitter<Long> emitter) ->
                        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                                .take(Integer.MAX_VALUE)
                                .subscribe(emitter::onNext)
                ).observeOn(Schedulers.newThread());


        //创建一个"热"的被观察者
        ConnectableObservable<Long> hot = Observable
                .create((ObservableEmitter<Long> emitter) ->
                        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                                .take(Integer.MAX_VALUE)
                                .subscribe(emitter::onNext)
                )
                .observeOn(Schedulers.newThread())
                .publish();
        //执行完这一步才真正变成hot
        hot.connect();


        //借助Subject 把Cold Observable 转化成 Hot Observable
        PublishSubject<Long> subject = PublishSubject.create();
        cold.subscribe(subject);

//        test(cold);
//        test(hot);
        test(subject);//subject为 Hot Observable
    }

    private static void test(Observable observable) {
        Consumer<Long> consumer1 = newConsumer("consumer1: ");
        Consumer<Long> consumer2 = newConsumer("    consumer2: ");
        Consumer<Long> consumer3 = newConsumer("        consumer3: ");

        observable.subscribe(consumer1);
        observable.subscribe(consumer2);
        sleep(30);
        observable.subscribe(consumer3);
        sleep(100);
    }

    private static <T> Consumer<T> newConsumer(String info) {
        return o -> System.out.println(info + o);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
