package com.demo.plugindevelop.annotation;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 13:24
 * description :
 */
public class ClickInvocationHandler implements InvocationHandler {

    private Context context;
    private Method clickMethod;

    public ClickInvocationHandler(Context context, Method clickMethod) {
        this.context = context;
        this.clickMethod = clickMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return clickMethod.invoke(context, args);
    }
}
