package com.demo.plugindevelop.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 12:17
 * description :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(callbackMethodName = "onClick",
        callbackSetterName = "setOnClickListener",
        interfaceClass = View.OnClickListener.class)
public @interface OnClick {

    // default 默认值
    int[] value() default -1;

}
