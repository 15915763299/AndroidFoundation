package com.demo.net.okhttp;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 22:40
 * description :
 */
public class OkHttpInstance {

    private static class Instance {
        private static OkHttpInstance instance = new OkHttpInstance();
    }

    public static OkHttpClient get() {
        return Instance.instance.okHttpClient;
    }

    private OkHttpClient okHttpClient;

    private OkHttpInstance() {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = httpBuilder
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .build();
    }
}
