package com.demo.net.rxjava.demo;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 21:11
 * description : 利用 BehaviorSubject 特性进行预读（会保留订阅前一个数据）
 */
public class  RxPreLoader<T> {

    private BehaviorSubject<T> mData;

    private Disposable disposable;

    public RxPreLoader(T defaultValue) {
        mData = BehaviorSubject.createDefault(defaultValue);
    }

    public void publish(T obj) {
        mData.onNext(obj);
    }

    public Disposable subscribe(Consumer onNext) {
        disposable = mData.subscribe(onNext);
        return disposable;
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public BehaviorSubject<T> getCache() {
        return mData;
    }

    public T getLastCacheData() {
        return mData.getValue();
    }
}
