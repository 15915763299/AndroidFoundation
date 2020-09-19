package com.demo.imageandcache;

import android.app.Application;
import android.content.Context;

/**
 * @author 尉迟涛
 * create time : 2019/11/13 22:44
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
