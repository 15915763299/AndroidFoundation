package com.demo.plugindevelop.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 12:17
 * description : 长按事件
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(callbackMethodName = "onLongClick",
        callbackSetterName = "setOnLongClickListener",
        interfaceClass = View.OnLongClickListener.class)
public @interface OnLongClick {

    // default 默认值
    int[] value() default -1;

}
