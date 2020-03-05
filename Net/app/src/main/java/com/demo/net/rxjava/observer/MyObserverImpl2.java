package com.demo.net.rxjava.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 11:11
 * description :
 */
public class MyObserverImpl2 implements Observer {//MyObserver {

    private int count = 0;

//    @Override
//    public void update() {
//        count++;
//    }

    @Override
    public String toString() {
        return "count: " + count;
    }

    @Override
    public void update(Observable o, Object arg) {
        count++;
    }
}
