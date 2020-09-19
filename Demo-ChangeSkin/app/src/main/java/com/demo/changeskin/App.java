package com.demo.changeskin;

import android.app.Application;
import android.content.Context;

/**
 * @author 尉迟涛
 * create time : 2020/4/10 20:47
 * description :
 */
public class App extends Application {

    private static App app;

    public static App getApp() {
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        app = this;
    }
}
