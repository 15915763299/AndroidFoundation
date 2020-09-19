package com.demo.net.okhttp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author 尉迟涛
 * create time : 2020/2/27 17:25
 * description : 注意一个应用只实例化一个client喔
 * test 目录下有代理相关的用法
 */
public class OkhttpSample {

    private static final String URL = "";


    public static void main(String[] args) {
        // 设置代理
        Proxy proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress("111.111.111.111", 9999)
        );

        // 设置代理验证用户名密码
        Authenticator authenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                return response.request().newBuilder().addHeader(
                        "Proxy-Authorization",
                        Credentials.basic("username", "password")
                ).build();
            }
        };

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                //.addInterceptor() // 添加自定义拦截器，这个在最开始执行
                //.addNetworkInterceptor() // 这个在ConnectInterceptor之后执行
                //.cache() // 自定义缓存
                //.cookieJar() // 自定义cookie存取
                //.proxy(proxy) // 代理网址
                //.proxyAuthenticator(authenticator) // 代理网址需要验证的信息
                //.authenticator() // 服务器需要的验证信息
                //.connectionPool() // 自定义连接池
                .retryOnConnectionFailure(false)//是否重试
                .connectTimeout(60, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(60, TimeUnit.SECONDS);//设置写入超时时间
        OkHttpClient okHttpClient = clientBuilder.build();


        Request.Builder requestBuilder = new Request.Builder()
                .addHeader("Connection", "close");//okhttp的坑
        Request request = requestBuilder.url(URL).get().build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    //如果我们不需要自己的App中的请求走代理，则可以配置一个proxy(Proxy.NO_PROXY)，
    // 这样也可以避免被抓包。NO_PROXY的定义如下：
    public static final Proxy NO_PROXY = new Proxy(Proxy.Type.DIRECT, null);

    //`DIRECT`：无代理，`HTTP`：http代理，`SOCKS`：socks代理
    // 对于Socks代理，在HTTP的场景下，代理服务器完成TCP数据包的转发工作;
    // 而Http代理服务器，在转发数据之外，还会解析HTTP的请求及响应，并根据请求及响应的内容做一些处理。

    // 代理这块比较复杂，如果需要用到请专门研究
}
