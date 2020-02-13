package com.demo.plugindevelop;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Environment;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * @author 尉迟涛
 * create time : 2020/2/13 9:39
 * description :
 */
public class LoadUtil {

    /**
     * 这里使用 Android Studio 自带的模拟器，有 /sdcard 目录
     */
    private final static String PLUGIN_PATH = "/sdcard/plugin-debug.apk";

    public static void loadClass(Context context) {

        try {
            // BaseDexClassLoader 的 findClass 方法在 pathList 中寻找类
            // BaseDexClassLoader 类中保存了 DexPathList pathList 对象
            // pathList 的 Filed 对象
            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            // DexPathList 以 Element[] dexElements 保存各个Java类
            // dexElement 的 Filed 对象
            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);


            // 获取宿主的 ClassLoader
            ClassLoader pathClassLoader = context.getClassLoader();
            // 获取 pathList 对象
            Object hostPathList = pathListField.get(pathClassLoader);
            // 获取宿主的 dexElement
            Object[] hostDexElements = (Object[]) dexElementsField.get(hostPathList);


            // 创建插件的 DexClassLoader
            ClassLoader dexClassLoader = new DexClassLoader(
                    PLUGIN_PATH, context.getCacheDir().getAbsolutePath(),
                    null, pathClassLoader
            );
            // 获取 pathList 对象
            Object pluginPathList = pathListField.get(dexClassLoader);
            // 获取插件的 dexElements
            Object[] pluginDexElements = (Object[]) dexElementsField.get(pluginPathList);


            //创建新数组
            Object[] dexElements = (Object[]) Array.newInstance(
                    hostDexElements.getClass().getComponentType(),
                    hostDexElements.length + pluginDexElements.length
            );
            // 合并 dexElements
            System.arraycopy(hostDexElements, 0, dexElements, 0, hostDexElements.length);
            System.arraycopy(pluginDexElements, 0, dexElements, hostDexElements.length, pluginDexElements.length);
            //替换宿主的 dexElements
            dexElementsField.set(hostPathList, dexElements);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
