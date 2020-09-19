package com.demo.plugindevelop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 11:50
 * description :
 */
// 用于描述域 Field declaration (includes enum constants)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {

    int value();

}
