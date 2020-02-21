package com.demo.plugin;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @author 尉迟涛
 * create time : 2020/2/16 10:08
 * description :
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
        if (getApplication() != null && getApplication().getResources() != null) {
            Log.e("BaseActivity", "获取插件的Resource");
            return getApplication().getResources();
        }
        return super.getResources();
    }
}
