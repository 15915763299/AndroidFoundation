package com.demo.net.rxjava.demo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author 尉迟涛
 * create time : 2020/3/7 17:18
 * description :
 */
public class Demo {

    public static void main(String[] args) {
//        map();
//        flatmap();
//        concatmap();
        buffer();
    }

    private static void map() {
        // 采用RxJava基于事件流的链式操作
        final Disposable disposable = Observable
                .create(emitter -> {
                    // 1. 被观察者发送事件 = 参数为整型 = 1、2、3
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                })
                // 2. 使用Map变换操作符中的Function函数对被观察者发送的事件进行统一变换：整型变换成字符串类型
                .map(integer -> integer + "_map")
                // 3. 观察者接收事件时，是接收到变换后的事件 = 字符串类型
                .subscribe(System.out::println);
    }

    private static void flatmap() {
        // 采用RxJava基于事件流的链式操作
        final Disposable disposable = Observable
                .create(emitter -> {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                })
                // 采用flatMap（）变换操作符
                .flatMap(integer -> {
                    final List<String> list = new ArrayList<>();
                    for (int i = 10; i < 14; i++) {
                        list.add(integer + "_flatMap_" + i);
                        // 通过flatMap中将被观察者生产的事件序列先进行拆分，
                        // 再将每个事件转换为一个新的发送三个String事件
                        // 最终合并，再发送给被观察者
                    }
                    return Observable.fromIterable(list);
                })
                .subscribe(System.out::println);
    }

    private static  void concatmap() {
        // 采用RxJava基于事件流的链式操作
        final Disposable disposable = Observable
                .create(emitter -> {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                })
                // 采用concatMap（）变换操作符
                .concatMap(integer -> {
                    final List<String> list = new ArrayList<>();
                    for (int i = 10; i < 14; i++) {
                        list.add(integer + "_concatMap_" + i);
                        // 通过concatMap中将被观察者生产的事件序列先进行拆分，
                        // 再将每个事件转换为一个新的发送三个String事件
                        // 最终合并，再发送给被观察者
                    }
                    return Observable.fromIterable(list);
                })
                .subscribe(System.out::println);
    }

    private static  void buffer() {
        // 被观察者 需要发送5个数字
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 1) // 设置缓存区大小 & 步长
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> stringList) {
                        //
                        System.out.println(" 缓存区里的事件数量 = " + stringList.size());
                        for (Integer value : stringList) {
                            System.out.println(" 事件 = " + value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("对Complete事件作出响应");
                    }
                });
    }


}
