package com.demo.net.rxjava.rxbus;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 19:04
 * description : 热被观察者的信息是共享的
 */
public class RxBus {

    private final FlowableProcessor<Object> mBus;

    private static class Holder{//单例
        private static final RxBus BUS = new RxBus();
    }

    private RxBus() {
        //  PublishProcessor<T> extends FlowableProcessor<T>
        // toSerialized 转成一个线程安全的操作
        mBus = PublishProcessor.create().toSerialized();

    }

    public static RxBus get(){
        return Holder.BUS;
    }

    public void post(Object obj) {//发送一个event
        mBus.onNext(obj);
    }

    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Flowable<Object> toFlowable() {
        return mBus;
    }

    public boolean hasSubscribers() {
        return false;
    }
}
