package com.demo.net.rxjava.demo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FromIterable {

    public static void main(String ... args){
            //TODO:
        fromIterable();
    }

    private static void fromIterable(){
      //TODO:
        // 1. 设置一个集合
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        // 2. 通过fromIterable()将集合中的对象 / 数据发送出去
        Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("集合遍历");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("集合中的数据元素 = "+ value  );
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
