package com.demo.net.rxjava.demo;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * Subject 既是观察者又是被观察者
 */
public class SubjectDemo {

    public static void main(String... args) {
//        asyncSubject();
//        behaviorSubject();
//        replaySubject();
        publishSubject();
//        publishSubject1();
    }

    /**
     * 只执行onComplete前的最后一个数据
     */
    private static void asyncSubject() {
        System.out.println("==============asyncSubject===============");

        AsyncSubject<String> subject = AsyncSubject.create();

        subject.onNext("async1");
        subject.onNext("async2");
//        subject.onComplete();

        subject.subscribe(
                s -> System.out.println("async: " + s),
                e -> System.out.println("async error"),
                () -> System.out.println("asyncSubject:complete")
        );

        subject.onNext("async3");
        subject.onNext("async4");
        subject.onComplete();
    }

    /**
     * 执行subscribe前的一个数据与之后的所有数据
     */
    private static void behaviorSubject() {
        System.out.println("==============behaviorSubject===============");

        BehaviorSubject<String> subject = BehaviorSubject.createDefault("behaviorSubject1");
        subject.onNext("behaviorSubject11");

        subject.subscribe(
                s -> System.out.println("behaviorSubject: " + s),
                e -> System.out.println("behaviorSubject error"),
                () -> System.out.println("behaviorSubject:complete")
        );

        subject.onNext("behaviorSubject2");
        subject.onNext("behaviorSubject3");
        subject.onNext("behaviorSubject4");
    }

    /**
     * 无论何时订阅，全部数据都会接收到
     */
    private static void replaySubject() {
        System.out.println("==============replaySubject===============");

        ReplaySubject<String> subject = ReplaySubject.create();

        // 缓存subscribe之前的几个数据，默认是全部缓存
//        ReplaySubject<String> subject = ReplaySubject.createWithSize(2);
        subject.onNext("replaySubject1");
        subject.onNext("replaySubject2");
        subject.onNext("replaySubject21");

        subject.subscribe(
                s -> System.out.println("replaySubject: " + s),
                e -> System.out.println("replaySubject error"),
                () -> System.out.println("replaySubject:complete")
        );

        subject.onNext("replaySubject3");
        subject.onNext("replaySubject4");
    }

    /**
     * 发送订阅之后的全部数据
     */
    public static void publishSubject() {
        System.out.println("==============publishSubject===============");

        PublishSubject<String> subject = PublishSubject.create();
        subject.onNext("publishSubject1");
        subject.onNext("publishSubject2");
//        subject.onComplete();

        subject.subscribe(
                s -> System.out.println("publishSubject: " + s),
                e -> System.out.println("publishSubject error"),
                () -> System.out.println("publishSubject:complete")
        );

        subject.onNext("publishSubject3");
        subject.onNext("publishSubject4");
        subject.onComplete();
    }

    public static void publishSubject1() {
        System.out.println("==============publishSubject1===============");

        PublishSubject<String> subject = PublishSubject.create();

        subject.subscribeOn(Schedulers.io()).subscribe(
                s -> System.out.println("publishSubject: " + s),
                e -> System.out.println("publishSubject error"),
                () -> System.out.println("publishSubject:complete")
        );

        subject.onNext("foo");
        subject.onNext("bar");
        subject.onComplete();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
