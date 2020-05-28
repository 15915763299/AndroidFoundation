package com.demo.checkmemory;

import android.app.Application;

import leakcanary.LeakCanary;

/**
 * @author 尉迟涛
 * create time : 2020/4/25 22:10
 * description :
 *
 * 全新 LeakCanary 2 ! 完全基于 Kotlin 重构升级 ！
 * https://blog.csdn.net/u012551350/article/details/97079164
 *
 * LeakCanary 与 鹅场Matrix ResourceCanary对比分析
 * https://www.cnblogs.com/sihaixuan/p/11140479.html
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.Config config = LeakCanary.getConfig().newBuilder()
                .retainedVisibleThreshold(1)
                .computeRetainedHeapSize(false)
                .build();
        LeakCanary.setConfig(config);
    }

}
