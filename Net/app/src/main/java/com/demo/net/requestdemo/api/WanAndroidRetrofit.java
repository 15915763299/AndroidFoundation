package com.demo.net.requestdemo.api;

import com.demo.net.okhttp.OkHttpInstance;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 23:01
 * description :
 */
public class WanAndroidRetrofit {

    private static String BASE_URL = "https://www.wanandroid.com/";

    private static class Instance {
        private static WanAndroidRetrofit instance = new WanAndroidRetrofit();
    }

    public static Retrofit get() {
        return WanAndroidRetrofit.Instance.instance.retrofit;
    }

    private Retrofit retrofit;

    public WanAndroidRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpInstance.get())
            //添加一个json的工具
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            //添加rxjava处理工具
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }
}
