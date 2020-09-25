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

    /**
     * 拦截器顺序： RealCall # getResponseWithInterceptorChain
     *
     * 1、自定义拦截器
     * interceptors.addAll(client.interceptors());
     * 2、重试拦截器
     * interceptors.add(retryAndFollowUpInterceptor);
     * 3、桥接拦截器
     * interceptors.add(new BridgeInterceptor(client.cookieJar()));
     * 4、缓存拦截器
     * interceptors.add(new CacheInterceptor(client.internalCache()));
     * 5、连接(池)拦截器
     * interceptors.add(new ConnectInterceptor(client));
     * 6、自定义network拦截器
     * if (!forWebSocket) {
     *      interceptors.addAll(client.networkInterceptors());
     * }
     * 7、请求服务拦截器
     * interceptors.add(new CallServerInterceptor(forWebSocket));
     */
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
