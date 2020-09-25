package com.demo.net.rxjava.observer;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 11:11
 * description :
 */
public class MyObserverImpl implements MyObserver {

    private int count = 0;

    @Override
    public void update() {
        count++;
    }

    @Override
    public String toString() {
        return "count: " + count;
    }
}
