package com.demo.plugindevelop.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 17:19
 * description :
 */
public class ReflectUtils {

    public static void main(String[] args) {
        // 创建对象
        String className = "com.demo.plugindevelop.reflection.TestClass";
        Class[] types = {String.class};
        Object[] vales = {"test"};

        Object test = createObject(className, types, vales);
        System.out.println(test == null ? "null" : test);

        // 调用对象方法
        System.out.println("调用对象方法");
        Object result = invokeInstanceMethod(test, "getValue", null, null);
        System.out.println(result);

        // 调用静态方法
        System.out.println("调用静态方法");
        invokeStaticMethod(className, "print", types, new String[]{"-- myInfo--"});

        // 获取某个域的值
        System.out.println("获取某个域的值");
        System.out.println(getFieldObject(test, "value"));

        // 设置某个域的值
        System.out.println("设置某个域的值");
        setFiledObject(test, "value", "newValue");
        System.out.println(test);

    }

    /**
     * 调用构造方法，创建对象
     *
     * @param className
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getDeclaredConstructor(pareTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用对象的非静态方法
     *
     * @param obj
     * @param methodName
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] pareTypes, Object[] pareValues) {
        if (obj == null) {
            return null;
        }

        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(obj, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用类的静态方法
     *
     * @param className
     * @param methodName
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] pareTypes, Object[] pareValues) {
        try {
            Class clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(null, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取对象指定域的值
     *
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getFieldObject(Object obj, String filedName) {
        if (obj != null) {
            try {
                Field field = obj.getClass().getDeclaredField(filedName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setFiledObject(Object obj, String filedName, Object valueObj) {
        if (obj == null) {
            return;
        }

        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, valueObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
