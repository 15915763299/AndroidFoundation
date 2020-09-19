package com.demo.plugindevelop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 12:06
 * description :
 */
// Annotation type declaration
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME  )
public @interface EventBase {

    // 设置监听的方法
    String callbackSetterName();

    // 监听事件接口类型
    Class<?> interfaceClass();

    // 回调方法名
    String callbackMethodName();

}
