package com.demo.changeskin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demo.changeskin.part.ActPart;
import com.demo.changeskin.replaceattr.ActReplaceAttr;
import com.demo.changeskin.replaceview.ActReplaceView;
import com.demo.changeskin.custompaint.ActCustomPaint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.demo.changeskin.replaceattr.ActReplaceAttr.APK_FILE_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String path;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);

        button = findViewById(R.id.btn_write);
        path = getFilesDir().getAbsolutePath() + File.separator + APK_FILE_NAME;
        File file = new File(path);
        if (file.exists()) {
            button.setEnabled(false);
        } else {
            button.setOnClickListener(this);
        }

        requestPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this, ActReplaceView.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, ActReplaceAttr.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(MainActivity.this, ActCustomPaint.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(MainActivity.this, ActPart.class));
                break;
            case R.id.btn_write:
                writeApkToExternalStorage();
                break;
            default:
        }
    }

    private void writeApkToExternalStorage() {
        button.setEnabled(false);
        FileOutputStream fos;
        InputStream is;
        try {
            is = getAssets().open(APK_FILE_NAME);
            Log.e("MainActivity", path);
            fos = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();// 保存数据

            Toast.makeText(this, "成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "失败", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            e.printStackTrace();
        }
    }

    void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
            }
        }
    }

}
