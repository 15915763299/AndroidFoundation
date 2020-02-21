package com.demo.plugin;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // style 要改成 NoActionBar 的，否则会报 setWindowCallback on null ref
        setContentView(R.layout.activity_main);
//        int layoutId = getResources().getIdentifier(
//                "activity_main",
//                "layout",
//                "com.demo.plugin"
//        );
//        setContentView(layoutId);
        Log.e("MainActivity", "我是插件的 MainActivity");
    }
}
