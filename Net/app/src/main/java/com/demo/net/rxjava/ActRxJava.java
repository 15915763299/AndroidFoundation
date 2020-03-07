package com.demo.net.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.net.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 尉迟涛
 * create time : 2020/3/5 20:40
 * description :
 */
public class ActRxJava extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ActRxJava.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_java);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3_1).setOnClickListener(this);
        findViewById(R.id.btn3_2).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (disposable != null) {
            disposable.dispose();
        }
        switch (v.getId()) {
            case R.id.btn1:
                rxJavaDemo();
                break;
            case R.id.btn2:
                map();
                break;
            case R.id.btn3_1:
                backPressure1_1();
                break;
            case R.id.btn3_2:
                backPressure1_2();
                break;
            case R.id.btn4:
                backPressure2();
                break;
            case R.id.btn5:
                backPressure3();
                break;
            case R.id.btn6:
                backPressure4();
                break;
            default:
        }
    }

    public void rxJavaDemo() {
        // 1. 创建一个被观察者
        Observable<String> observable = Observable.create((ObservableEmitter<String> emitter) -> {
            Log.i(TAG, "subscribe: " + Thread.currentThread().getName());
            emitter.onNext("test1");
//          emitter.onComplete();
        });

        //2. 创建一个观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: " + Thread.currentThread().getName());
                Log.i(TAG, "onSubscribe: " + d);
            }

            @Override
            public void onNext(String str) {
                Log.i(TAG, "onNext: " + Thread.currentThread().getName());
//                sleep(5_000);
                Log.i(TAG, "onNext: " + str);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + Thread.currentThread().getName());
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: " + Thread.currentThread().getName());
                Log.i(TAG, "onComplete: ");
            }
        };


//        map(new Function<String, String>() {
//            @Override
//            public String apply(String s) throws Exception {
//                Log.i(TAG, "subscribeOn上游: " + Thread.currentThread().getName());
//                return s + "map";
//            }
//        })

        observable
                .map(s -> {
                    Log.i(TAG, "subscribeOn上游apply: " + Thread.currentThread().getName());
                    return s + "map";
                })
                // 都生效了，但是结果按第一次的执行
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.single())
                .map(s -> {
                    Log.i(TAG, "subscribeOn下游apply: " + Thread.currentThread().getName());
                    return "1" + s;
                })
                // 都生效了，但是结果按最后一次的执行(observeOn：onNext等，发生在指定线程，下游发生在指定线程)
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(s -> {
                    Log.i(TAG, "observeOn下游apply: " + Thread.currentThread().getName());
                    return "1" + s;
                })
                // 订阅
                .subscribe(observer);
    }

    private void map() {
//        Observable.just("test")
//                .map(new Function<String,String>(){
//                    @Override
//                    public String apply(String s) throws Exception {
//                        return s + "map";
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.i(TAG,"s: " + s);
//                    }
//                });

        disposable = Observable.just("test")
                .map((String s) -> s + "map")
                .subscribe((String s) -> Log.i(TAG, "s: " + s));
    }

    /**
     * 背压：
     * 发送太快太多，处理太慢，出现背压
     * 先决条件：异步，上游产生数据的速度比下游处理数据的速度快
     * rxjava2测试
     */
    private void backPressure1_1() {
        // RXJava2.0中Observable不再支持背压,既然说Observable不再支持背压，
        // 那么我们随便搞应该就不会报哪个MissingBackPressureException
        disposable = Observable.<Integer>create(
                emitter -> {
                    for (int i = 0; ; i++) {   //无限循环发事件
                        emitter.onNext(i);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Integer integer) -> {
                    Log.i(TAG, "call: " + integer);
                    sleep(10);//背压出现
                });
    }

    /**
     * rxjava1测试
     */
    private void backPressure1_2() {
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                for (int i = 0; ; i++) {   //无限循环发事件
//                    subscriber.onNext(i);
//                }
//            }
//        }).subscribeOn(Schedulers.io())
////           .observeOn(Schedulers.newThread())
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        System.out.println("call: " + integer);
//                    }
//                });
    }

    /**
     * 解决方案1 同一线程 处理+回调，但是这是UI线程啊。。。，而且内存抖动会比较厉害
     */
    public void backPressure2() {
        disposable = Observable.<Integer>create(
                emitter -> {
                    for (int i = 0; ; i++) {
                        emitter.onNext(i);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    Log.i(TAG, "call: " + i);
                    sleep(10);
                });
    }

    /**
     * 解决方案2 部分取样
     */
    public void backPressure3() {
        disposable = Observable.<Integer>create(
                emitter -> {
                    for (int i = 0; ; i++) {
                        emitter.onNext(i);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .sample(1, TimeUnit.SECONDS)
                .subscribe(i -> {
                    Log.i(TAG, "call: " + i);
                    sleep(10);
                });
    }

    Subscription subscription;

    /**
     * Rx2中才有处理背压的问题——Flowable
     * 背压策略：BackpressureStrategy
     * BUFFER缓存、ERROR抛异常、DROP丢弃、LATEST丢弃旧的
     * 有点像线程池的拒绝策略
     * https://www.jianshu.com/p/220955eefc1f
     */
    public void backPressure4() {
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR)
        Flowable.range(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        // 使用subscription控制流速
                        subscription = s;
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }



//    private static void flowabledemo() {
//        /*
//         * 不设置Subscription的情况
//         * 查看源码可知RxJava2.0以上，Flowable的背压大小为128
//         * 测试结果：
//         * 1、使用Flowable不设置subscription.request();的情况下，下游无法收到数据。
//         * 2、使用Flowable不设置subscription.request();的情况下，上游发送数据超过128，
//         * 报出MissingBackpressureException。
//         */
//
//        Flowable
//                .create(new FlowableOnSubscribe<Integer>() {
//                    @Override
//                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                        for (int i = 0; i < 129; i++) {
//                            System.out.println("emitter=" + i);
//                            emitter.onNext(i);
//                        }
//                    }
//                }, BackpressureStrategy.MISSING)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(Long.MAX_VALUE);
//                        System.out.println("onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println("onNext=" + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        System.out.println("onError=" + t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("onComplete");
//                    }
//                });
//
//    }
}
