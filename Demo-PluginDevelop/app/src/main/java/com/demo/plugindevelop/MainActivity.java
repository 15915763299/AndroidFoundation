package com.demo.plugindevelop;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.demo.plugindevelop.annotation.ActAnnotation;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);

        // Boot
        Log.e(TAG, "Activity: " + Activity.class.getClassLoader());
        // Path
        Log.e(TAG, "AppCompatActivity: " + AppCompatActivity.class.getClassLoader());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (requestPermission()) {
                    loadStaticPlugin();
                    loadPluginActivity();
                }
                break;
            case R.id.btn2:
                Intent intent = new Intent(this, ActAnnotation.class);
                startActivity(intent);
                break;
            default:
        }
    }

    /**
     * 权限是否已通过
     */
    private boolean requestPermission() {
        Log.e(TAG, "checkPermission");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "requestPermission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadStaticPlugin();
                loadPluginActivity();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 加载静态插件
     * 需要将插件 apk 放置到 LoadUtil 中的指定路径下
     */
    private void loadStaticPlugin() {
        Log.e(TAG, "loadStaticPlugin");
        // 加载插件dex
        LoadUtil.loadClass(this);

        // 运行插件代码
        try {
            Class<?> staticPluginClass = Class.forName("com.demo.plugin.StaticPlugin");
            Method printMethod = staticPluginClass.getMethod("print");
            printMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载插件中的 Activity
     * 需要将插件 apk 放置到 LoadUtil 中的指定路径下
     * 记得加载 Resources， LoadUtil.loadResources(this);
     */
    private void loadPluginActivity() {
        String packageName = "com.demo.plugin";
        String className = "com.demo.plugin.MainActivity";

        HookUtil.hookAms(packageName, className);
        HookUtil.hookHandler();

        // 启动插件 Activity
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, className));
        startActivity(intent);
    }
}
