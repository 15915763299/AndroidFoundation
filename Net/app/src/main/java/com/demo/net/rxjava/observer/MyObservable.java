package com.demo.net.rxjava.observer;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 11:01
 * description :
 */
public interface MyObservable {

    /**
     * 添加观察者
     * @param observer
     */
    void add(MyObserver observer);

    /**
     * 删除观察者
     * @param observer
     */
    void remove(MyObserver observer);

    /**
     * 产生变化，通知观察者
     */
    void notifyObservers();
}
