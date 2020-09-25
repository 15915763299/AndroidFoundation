package com.demo.net.rxjava.demo;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FromArrayDemo {

    private static final String TAG = "FromArrayDemo";

    public static void main(String ... args){
            //TODO:
        fromArray();
    }

    public static void fromArray(){
        // 1. 设置需要传入的数组
        Integer[] items = { 0, 1, 2, 3, 4 };

        // 2. 创建被观察者对象（Observable）时传入数组
        // 在创建后就会将该数组转换成Observable & 发送该对象中的所有数据
        Observable.fromArray(items)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("数组遍历");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("数组中的元素 = "+ value  );
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("遍历结束");
                    }

                });

    }
}
