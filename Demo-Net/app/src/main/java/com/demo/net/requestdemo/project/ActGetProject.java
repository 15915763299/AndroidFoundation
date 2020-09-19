package com.demo.net.requestdemo.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.demo.net.R;
import com.demo.net.databinding.ActGetProjectBinding;
import com.demo.net.requestdemo.api.WanAndroidApi;
import com.demo.net.requestdemo.api.WanAndroidRetrofit;
import com.demo.net.requestdemo.bean.ProjectBean;
import com.demo.net.utils.JsonUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 尉迟涛
 * create time : 2020/3/7 17:23
 * description :
 */
public class ActGetProject extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActGetProjectBinding binding;
    private WanAndroidApi wanAndroidApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_get_project);

        wanAndroidApi = WanAndroidRetrofit.get().create(WanAndroidApi.class);

        retryWhen();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable = null;
        super.onDestroy();
    }

    //    private int count = 0;
    // 可重试次数
    private int maxConnectCount = 10;
    // 当前已重试次数
    private int currentRetryCount = 0;
    // 重试等待时间
    private int waitRetryTime = 0;

//    public void retry() {
//        compositeDisposable.add(wanAndroidApi.getProject()
//                .retry(throwable -> {
//                    if (count < 4) {
//                        System.out.println("重试： " + ++count);
//                        return true;
//                    }
//                    return false;
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(projectBean -> System.out.println("retry: " + projectBean))
//        );
//    }

    public void retryWhen() {
        Observable<ProjectBean> observable = wanAndroidApi.getProject();

        // 步骤4：发送网络请求 & 通过retryWhen（）进行重试
        // 注：主要异常才会回调retryWhen（）进行重试
        observable
                .retryWhen(throwableObservable -> {
                    // 参数Observable<Throwable>中的泛型 = 上游操作符抛出的异常，可通过该条件来判断异常的类型
                    return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {

                            // 输出异常信息
                            System.out.println("发生异常 = " + throwable.toString());
                            /*
                             * 需求1：根据异常类型选择是否重试
                             * 即，当发生的异常 = 网络异常 = IO异常 才选择重试
                             */
                            if (throwable instanceof IOException) {

                                System.out.println("属于IO异常，需重试");
                                /*
                                 * 需求2：限制重试次数
                                 * 即，当已重试次数 < 设置的重试次数，才选择重试
                                 */
                                if (currentRetryCount < maxConnectCount) {

                                    // 记录重试次数
                                    currentRetryCount++;
                                    System.out.println("重试次数 = " + currentRetryCount);
                                    /*
                                     * 需求2：实现重试
                                     * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen（）重订阅，最终实现重试功能
                                     *
                                     * 需求3：延迟1段时间再重试
                                     * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
                                     *
                                     * 需求4：遇到的异常越多，时间越长
                                     * 在delay操作符的等待时间内设置 = 每重试1次，增多延迟重试时间1s
                                     */
                                    // 设置等待时间
                                    waitRetryTime = 1000 + currentRetryCount * 1000;
                                    System.out.println("等待时间 =" + waitRetryTime);
                                    return Observable.just(1).delay(waitRetryTime, TimeUnit.MILLISECONDS);
                                } else {
                                    // 若重试次数已 > 设置重试次数，则不重试
                                    // 通过发送error来停止重试（可在观察者的onError（）中获取信息）
                                    return Observable.error(new Throwable(
                                            "重试次数已超过设置次数 = " + currentRetryCount + "，即 不再重试"));
                                }
                            }

                            // 若发生的异常不属于I/O异常，则不重试
                            // 通过返回的Observable发送的事件 = Error事件 实现（可在观察者的onError（）中获取信息）
                            else {
                                return Observable.error(new Throwable(
                                        "发生了非网络异常（非I/O异常）"));
                            }
                        }
                    });
                })
                .subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求结果
                .subscribe(new Observer<ProjectBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ProjectBean result) {
                        // 接收服务器返回的数据
                        System.out.println("发送成功");
                        binding.tx.setText(JsonUtil.formatJson(JsonUtil.toJson(result)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 获取停止重试的信息
                        System.out.println(e.toString());
                        binding.tx.setText(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
