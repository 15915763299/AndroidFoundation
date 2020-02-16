package com.demo.plugindevelop;

import android.app.Application;
import android.content.res.Resources;

/**
 * @author 尉迟涛
 * create time : 2020/2/16 10:05
 * description :
 */
public class App extends Application {

    private Resources resources;

    @Override
    public Resources getResources() {
        return resources == null ? super.getResources() : resources;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        resources = LoadUtil.loadResources(this);
    }
}
