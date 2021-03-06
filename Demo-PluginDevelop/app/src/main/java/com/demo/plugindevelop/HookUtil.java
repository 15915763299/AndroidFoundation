package com.demo.plugindevelop;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/2/13 15:20
 * description : 基于 Android 9.0，适配6.0至9.0
 */
public class HookUtil {

    private static final String TAG = HookUtil.class.getSimpleName();
    private static final String TARGET_INTENT = "target_intent";

    /**
     * Activity # startActivity
     * Instrumentation # execStartActivity
     * ActivityManager.getService().startActivity(...)
     * getService获取的是IActivityManager对象
     *
     * 1、通过静态方法获取获取IActivityManager对象
     * 2、动态代理该对象，为startActivity方法插入一些代码
     * 3、使用动态代理对象替换原有对象
     *
     * @param packageName 插件Activity包名
     * @param className 插件Activity类名
     */
    public static void hookAms(final String packageName, final String className) {
        try {
            Field singletonField;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 获取 ActivityManager 中的 IActivityManagerSingleton
                Class<?> clazz = Class.forName("android.app.ActivityManager");
                singletonField = clazz.getDeclaredField("IActivityManagerSingleton");
            } else {
                // 8.0 以前是 ActivityManagerNative 中的 gDefault
                Class<?> clazz = Class.forName("android.app.ActivityManagerNative");
                singletonField = clazz.getDeclaredField("gDefault");
            }

            singletonField.setAccessible(true);
            // IActivityManagerSingleton
            Object singleton = singletonField.get(null);

            // 获取 Singleton 中的 mInstance
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            // 1、IActivityManager
            final Object mInstance = mInstanceField.get(singleton);

            Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
            // 2、动态代理 IActivityManager 的 Class 对象
            Object proxyInstance = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManagerClass},
                    (Object proxy, Method method, Object[] args) -> {
                        // 动态代理IActivityManager，如果是startActivity方法，插入代码，替换intent
                        if ("startActivity".equals(method.getName())) {
                            int index = 0;

                            // 找到该方法参数中的 Intent
                            for (; index < args.length; index++) {
                                if (args[index] instanceof Intent) {
                                    break;
                                }
                            }

                            Intent intent = (Intent) args[index];

                            // 当前intent为插件 Activity 的 Intent，使用代理intent
                            ComponentName component = intent.getComponent();
                            if (component != null &&
                                    component.getPackageName().equals(packageName) &&
                                    component.getClassName().equals(className)) {
                                // 创建代理的Intent
                                Intent proxyIntent = new Intent();
                                proxyIntent.setClassName(
                                        "com.demo.plugindevelop",
                                        ProxyActivity.class.getName()
                                );

                                // 将插件 Activity 的 Intent 保存到代理 Activity 的 Intent 中
                                proxyIntent.putExtra(TARGET_INTENT, intent);

                                // 将插件 Activity 的 Intent 替换为代理 Activity 的 Intent
                                args[index] = proxyIntent;
                            }
                        }

                        // 调用IActivityManager中原有的方法
                        return method.invoke(mInstance, args);
                    }
            );

            // 动态代理，替换原对象为代理对象，singleton 中的 mInstance 换为 proxyInstance
            mInstanceField.set(singleton, proxyInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取mH对象，设置一个callback
     */
    public static void hookHandler() {
        try {
            // 获取 ActivityThread 对象
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = clazz.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object activityThread = sCurrentActivityThreadField.get(null);

            // 获取 ActivityThread.H 对象
            Field mHField = clazz.getDeclaredField("mH");
            mHField.setAccessible(true);
            Object mH = mHField.get(activityThread);

            // 获取 ActivityThread.H 的 callback field
            Class<?> handlerClass = Class.forName("android.os.Handler");
            Field mCallbackField = handlerClass.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            // 插入 Callback
            mCallbackField.set(mH, (Handler.Callback) msg -> {
                switch (msg.what) {
                    // LAUNCH_ACTIVITY = 100;
                    case 100:
                        changeIntent6(msg.obj);
                        break;
                    //9.0起 EXECUTE_TRANSACTION = 159;
                    case 159:
                        changeIntent9(msg.obj);
                        break;
                    default:
                }
                // 是否消费掉事件，不能消费掉，不然mH的基本功能就没了
                return false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 6.0 至 9.0 系统
     */
    private static void changeIntent6(Object obj) {
        // obj 为 ActivityClientRecord
        try {
            // 获取代理 Activity 的 Intent
            Field intentField = obj.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent proxyIntent = (Intent) intentField.get(obj);

            // 获取插件 Activity 的 Intent
            Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
            // 判断调用的是否是插件的，如果不是插件的，intent就会为空
            if (intent != null) {
                Log.e(TAG, "** change intent **");
                intentField.set(obj, intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 9.0 及以后的系统
     */
    private static void changeIntent9(Object obj) {
        // obj 为 ClientTransaction
        try {
            // 获取代理 ClientTransaction 中的 mActivityCallbacks
            Field callBacksField = obj.getClass().getDeclaredField("mActivityCallbacks");
            callBacksField.setAccessible(true);
            List mActivityCallbacks = (List) callBacksField.get(obj);

            if (mActivityCallbacks != null && mActivityCallbacks.size() > 0) {
                Log.e(TAG, "start to find the proxy intent");
                // 获取 mActivityCallbacks 中的 LaunchActivityItem
                Class<?> launchItemClass = Class.forName("android.app.servertransaction.LaunchActivityItem");
                Object launchActivityItem = mActivityCallbacks.get(0);

                if (launchItemClass.isInstance(launchActivityItem)) {
                    Log.e(TAG, "** find the proxy intent **");
                    // 拿到代理 Activity 的 Intent
                    Field intentField = launchItemClass.getDeclaredField("mIntent");
                    intentField.setAccessible(true);
                    Intent proxyIntent = (Intent) intentField.get(launchActivityItem);

                    // 获取插件 Activity 的 Intent
                    Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                    // 判断调用的是否是插件的，如果不是插件的，intent就会为空
                    if (intent != null) {
                        Log.e(TAG, "** change intent **");
                        intentField.set(launchActivityItem, intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
