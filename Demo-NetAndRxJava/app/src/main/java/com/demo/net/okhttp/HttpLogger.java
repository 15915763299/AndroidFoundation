package com.demo.net.okhttp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.net.utils.JsonUtil;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 22:51
 * description : log 拦截器回调
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(@NonNull String message) {
        // 请求或者响应开始
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0);
        }

        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = JsonUtil.formatJson(message);
        }
        mMessage.append(message.concat("\n"));

        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            Log.d("Zero", mMessage.toString());
        }
    }
}
