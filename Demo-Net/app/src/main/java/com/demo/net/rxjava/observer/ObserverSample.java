package com.demo.net.rxjava.observer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 10:37
 * description : 观察者模式（注意与发布订阅者模式不一样！）
 *
 * RxJava https://www.jianshu.com/p/88aacbed8aa5
 */
public class ObserverSample {

    public static void main(String[] args) {

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //emitter 为 CreateEmitter 对象
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onComplete();
            }
        });
        // 创建的是 ObservableCreate
        System.out.println(observable.getClass());

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //d 为 CreateEmitter 对象
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        // observable 是作为数据源，所以作为调用者
        // 被观察者 需要调用 观察者 的方法
        // 逻辑上应该是 observer 订阅 observable
        observable.subscribe(observer);
        //// 指定事件源代码执行的线程（Observable、doOnCompleted()、doOnError()）
        //.subscribeOn(Schedulers.io())
        //// 指定doOnUnsubscribe()的线程。参考：https://www.jianshu.com/p/ce5254b8c12d
        //.unsubscribeOn(Schedulers.io())
        //// 指定订阅者代码执行的线程
        //.observeOn(AndroidSchedulers.mainThread());
        //判断一个操作是否被取消
        //.doOnUnsubscribe(new Action0() {提示被取消了})

        //取消操作时只需要调用subscription.unsubscribe()即可：

        System.out.println("-------------------");

        MyObservable myObservable = new MyObservableImpl();
        MyObserver myObserver1 = new MyObserverImpl();
        MyObserver myObserver2 = new MyObserverImpl();
        MyObserver myObserver3 = new MyObserverImpl();

        myObservable.add(myObserver1);
        myObservable.add(myObserver2);
        myObservable.add(myObserver3);
        myObservable.notifyObservers();

        myObservable.remove(myObserver3);
        myObservable.notifyObservers();

        myObservable.remove(myObserver2);
        myObservable.notifyObservers();

        System.out.println(myObserver1);
        System.out.println(myObserver2);
        System.out.println(myObserver3);


        System.out.println("Java原生接口-------------------");

        java.util.Observable myObservable2 = new MyObservableImpl2();
        java.util.Observer myObserver21 = new MyObserverImpl2();
        java.util.Observer myObserver22 = new MyObserverImpl2();
        java.util.Observer myObserver23 = new MyObserverImpl2();

        myObservable2.addObserver(myObserver21);
        myObservable2.addObserver(myObserver22);
        myObservable2.addObserver(myObserver23);
        // !!! jdk 实现需要调用 protected void setChange
        myObservable2.notifyObservers();

        myObservable2.deleteObserver(myObserver23);
        myObservable2.notifyObservers();

        myObservable2.deleteObserver(myObserver22);
        myObservable2.notifyObservers();

        System.out.println(myObserver1);
        System.out.println(myObserver2);
        System.out.println(myObserver3);
    }

}
