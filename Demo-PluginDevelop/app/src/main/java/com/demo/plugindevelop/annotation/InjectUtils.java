package com.demo.plugindevelop.annotation;

import android.content.Context;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 9:25
 * description : 注解实现类
 */
public class InjectUtils {

    public static void inject(Context context) {
        injectLayout(context);
        injectView(context);
        injectClick(context);
    }

    /**
     * 1、通过反射遍历传入对象的method，判断每个method的注解是否包含点击事件的注解
     * 2、获取点击事件的关键元素（由注解传入）
     * 3、通过反射获取View的id、获取View
     * 4、使用反射调用setOnClickListener，参数为proxy
     */
    private static void injectClick(Context context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        // 1、查看每个 method 的每个 annotation
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // 根据 Annotation 获取 EventBase
                Class<?> annotationClass = annotation.annotationType();
                EventBase eventBase = annotationClass.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    return;
                }

                // 2、获取三要素
                // 设置回调接口的方法名（setOnClickListener）
                String callbackSetterName = eventBase.callbackSetterName();
                // 点击事件接口的class（View.OnClickListener.class）
                Class<?> interfaceClass = eventBase.interfaceClass();
                // String callbackMethodName = eventBase.callbackMethodName();

                try {
                    // 3、获取注解的相关方法，再根据这个方法获取view的id
                    Method annMethod = annotationClass.getDeclaredMethod("value");
                    int[] viewIds = (int[]) annMethod.invoke(annotation);

                    // 根据id获取view，设置监听器
                    for (int id : viewIds) {
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, id);
                        if (view == null) {
                            continue;
                        }

                        // 这里不直接生成监听接口对象，因为具体内容需要在activity中执行
                        // 所以使用动态代理
                        // 这里不是纯粹的动态代理，而是使用代理来替换方法
                        ClickInvocationHandler cih = new ClickInvocationHandler(context, method);
                        // 生成动态代理对象
                        Object proxy = Proxy.newProxyInstance(
                                interfaceClass.getClassLoader(),
                                new Class[]{interfaceClass},
                                cih
                        );

                        // setOnClickListener(proxy)
                        Method setter = view.getClass().getMethod(callbackSetterName, interfaceClass);
                        setter.invoke(view, proxy);
                        // 最终调用onClick的时候会调用到前面输入的method
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 1、通过反射遍历传入对象的参数，查看该参数是否被ViewInject注释
     * 2、通过反射调用findViewById，并将结果赋值给该参数
     */
    private static void injectView(Context context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value();

                try {
                    Method findViewById = clazz.getMethod("findViewById", int.class);
                    // 找到view
                    View view = (View) findViewById.invoke(context, viewId);
                    // 给activity中的view赋值
                    field.setAccessible(true);
                    field.set(context, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 其实这里用强转Activity也没啥毛病，尽量少用反射，这里只是一个Demo
     */
    private static void injectLayout(Context context) {
        Class<?> clazz = context.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                // 方法名 & 方法参数
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(context, layoutId);
//            } catch (NoSuchMethodException |
//                    IllegalAccessException |
//                    InvocationTargetException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
