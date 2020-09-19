package com.demo.net.rxjava.observer;

import java.util.Observable;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 11:04
 * description :
 */
public class MyObservableImpl2 extends Observable{ //MyObservable {

//    private HashSet<MyObserver> observers = new HashSet<>();


//    @Override
//    public synchronized void add(MyObserver observer) {
//        if (observer != null) {
//            observers.add(observer);
//        }
//    }
//
//    @Override
//    public synchronized void remove(MyObserver observer) {
//        if (observer != null) {
//            observers.remove(observer);
//        }
//    }

//    @Override
//    public synchronized void notifyObservers() {
//        for (MyObserver observer : observers) {
//            observer.update();
//        }
//    }


    @Override
    public void notifyObservers() {
        // !!! jdk 实现需要调用 setChange
        // setChanged();
        super.notifyObservers();
    }
}
