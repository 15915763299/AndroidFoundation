package com.demo.plugindevelop.annotation;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 13:24
 * description : 一层动态代理，调用对应对象的对应方法
 */
public class ClickInvocationHandler implements InvocationHandler {

    /**
     * 被注解的方法
     */
    private Method clickMethod;
    /**
     * 包含被注解方法的对象
     */
    private Context context;

    public ClickInvocationHandler(Context context, Method clickMethod) {
        this.context = context;
        this.clickMethod = clickMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return clickMethod.invoke(context, args);
        // 这里的 method 是 onClick 或 onLongClick，这里做了替换，换成了clickMethod，它是Activity内的方法
        // 所以这里不是纯粹的动态代理，而是做了代理并替换方法
//        return method.invoke(context, args);
    }
}
