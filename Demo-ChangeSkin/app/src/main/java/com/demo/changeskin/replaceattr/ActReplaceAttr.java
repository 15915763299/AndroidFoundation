package com.demo.changeskin.replaceattr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.changeskin.R;

import java.io.File;

/**
 * @author 尉迟涛
 * create time : 2020/4/7 9:54
 * description :
 */
public class ActReplaceAttr extends AppCompatActivity {

    public static String APK_FILE_NAME = "outresource-debug.apk";
    private SkinFactory skinFactory1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        skinFactory1 = new SkinFactory();
        skinFactory1.setDelegate(getDelegate());
        LayoutInflater.from(this).setFactory2(skinFactory1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_replace_attr);

        changeSkin();
    }

    public void changeSkin() {
        File skinFile = new File(getFilesDir(), APK_FILE_NAME);
        if (skinFile.exists()) {
            SkinEngine.getInstance().load(skinFile.getAbsolutePath(), this);
            skinFactory1.changeSkin();
        } else {
            String path = skinFile.getAbsolutePath();
            Log.e("ActSkinFactory1", path + ", not found apk");
        }
    }


}
