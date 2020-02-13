package com.demo.plugindevelop;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author 尉迟涛
 * create time : 2020/2/13 12:06
 * description :
 */
public class ProxyActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ProxyActivity", "我是宿主的 ProxyActivity");
    }
}
