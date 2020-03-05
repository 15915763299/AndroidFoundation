package com.demo.net.rxjava.observer;

import java.util.HashSet;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 11:04
 * description :
 */
public class MyObservableImpl implements MyObservable {

    private HashSet<MyObserver> observers = new HashSet<>();

    @Override
    public void add(MyObserver observer) {
        if(observer != null){
            observers.add(observer);
        }
    }

    @Override
    public void remove(MyObserver observer) {
        if(observer != null){
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (MyObserver observer : observers) {
            observer.update();
        }
    }
}
