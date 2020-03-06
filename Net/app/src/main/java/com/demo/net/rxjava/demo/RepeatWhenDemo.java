package com.demo.net.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RepeatWhenDemo {

    public static void main(String... args) {
        repeatWhen01();
    }

    private static void repeatWhen01() {
        Observable.just(1, 2, 4)
                .repeatWhen(objectObservable -> {
                    // 将原始 Observable 停止发送事件的标识（Complete() /  Error()）
                    // 转换成1个 Object 类型数据传递给1个新被观察者（Observable）
                    // 以此决定是否重新订阅 & 发送原来的 Observable

                    // 此处有2种情况：
                    // 1. 若新被观察者（Observable）返回1个Complete事件，则不重新订阅 & 发送原来的 Observable
                    // 2. 若新被观察者（Observable）返回1个Next事件，则重新订阅 & 发送原来的 Observable
                    return objectObservable.flatMap(throwable -> {

                        // 情况1：若新被观察者（Observable）返回1个Complete事件，则不重新订阅 & 发送原来的 Observable
//                        return Observable.empty();
                        // 返回Error
//                        return Observable.error(new Throwable("不再重新订阅事件"));
                        // 情况2：若新被观察者（Observable）返回1个Next事件，则重新订阅 & 发送原来的 Observable
                         return Observable.just(1);
                    });
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("对Error事件作出响应：" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("对Complete事件作出响应");
                    }

                });
    }
}
