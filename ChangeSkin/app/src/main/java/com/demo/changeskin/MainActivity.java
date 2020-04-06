package com.demo.changeskin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.demo.changeskin.x.SkinEngine;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);
        });
    }


//    public void changeSkin(){
//        File skinFile = new File(Environment.getExternalStorageDirectory(), "skin.apk");
//        SkinEngine.getInstance().load(skinFile.getAbsolutePath());
//        mSkinFactory.changeSkin();
//    }
}
