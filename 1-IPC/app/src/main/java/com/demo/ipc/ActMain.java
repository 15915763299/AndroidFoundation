package com.demo.ipc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.ipc.use.aidl.ActBookManager;
import com.demo.ipc.use.binderpool.client.ActBinderPool;
import com.demo.ipc.use.bundle.ActUseBundle;
import com.demo.ipc.use.file.ActUseFile;
import com.demo.ipc.use.messenger.ActUseMessenger;
import com.demo.ipc.use.provider.ActUseProvider;
import com.demo.ipc.use.socket.ActUseSocket;

public class ActMain extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);

        requestPermissions(98, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: {
                Intent intent = new Intent(this, ActUseBundle.class);
                startActivity(intent);
            }
            break;
            case R.id.btn2: {
                Intent intent = new Intent(this, ActUseFile.class);
                startActivity(intent);
            }
            break;
            case R.id.btn3: {
                Intent intent = new Intent(this, ActUseMessenger.class);
                startActivity(intent);
            }
            break;
            case R.id.btn4: {
                Intent intent = new Intent(this, ActBookManager.class);
                startActivity(intent);
            }
            break;
            case R.id.btn5: {
                Intent intent = new Intent(this, ActUseProvider.class);
                startActivity(intent);
            }
            break;
            case R.id.btn6: {
                Intent intent = new Intent(this, ActUseSocket.class);
                startActivity(intent);
            }
            break;
            case R.id.btn7: {
                Intent intent = new Intent(this, ActBinderPool.class);
                startActivity(intent);
            }
            break;
            default:
        }
    }

    public void requestPermissions(int requestCode, String permission) {
        if (permission != null && permission.length() > 0) {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    // 检查是否有权限
                    int hasPer = checkSelfPermission(permission);
                    if (hasPer != PackageManager.PERMISSION_GRANTED) {
                        // 是否应该显示权限请求
                        // boolean isShould = shouldShowRequestPermissionRationale(permission);
                        requestPermissions(new String[]{permission}, requestCode);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean flag = false;
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            requestPermissions(98, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
