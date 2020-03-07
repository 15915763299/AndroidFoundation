package com.demo.net;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * @author 尉迟涛
 * create time : 2020/3/6 22:48
 * description :
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
