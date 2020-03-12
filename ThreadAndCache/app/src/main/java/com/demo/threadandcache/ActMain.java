package com.demo.threadandcache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.threadandcache.cache.ActDiskLruCache;
import com.demo.threadandcache.cache.ActLruCache;
import com.demo.threadandcache.cache.glide.ActPictureFallsGlide;
import com.demo.threadandcache.cache.loader.ActPictureFalls;
import com.demo.threadandcache.thread.ActAsyncTask;
import com.demo.threadandcache.thread.ActEmpty;
import com.demo.threadandcache.thread.ActIntentService;
import com.demo.threadandcache.thread.ActThreadPriority;

import java.io.File;

/**
 * @author 尉迟涛
 * create time : 2020/2/1 13:23
 * description :
 */
public class ActMain extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ActMain.class.getSimpleName();

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

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
        findViewById(R.id.btn8).setOnClickListener(this);

        getDirTest();
    }

    @Override
    public void onClick(View v) {
        Class<?> clzz;
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                clzz = ActAsyncTask.class;
                break;
            case R.id.btn2:
                clzz = ActIntentService.class;
                break;
            case R.id.btn3:
                clzz = ActLruCache.class;
                break;
            case R.id.btn4:
                clzz = ActDiskLruCache.class;
                break;
            case R.id.btn5:
                clzz = ActPictureFalls.class;
                break;
            case R.id.btn6:
                clzz = ActPictureFallsGlide.class;
                break;
            case R.id.btn7:
                clzz = ActThreadPriority.class;
                break;
            case R.id.btn8:
                clzz = ActEmpty.class;
                break;
            default:
                clzz = null;
        }

        if (clzz != null) {
            Intent intent = new Intent(this, clzz);
            startActivity(intent);
        }
    }

    /**
     * 各路径获取方法，以获取及路径下可用空间
     */
    private void getDirTest() {
        File file;

        Log.d(TAG, "----------------------------");
        // /data
        // /data/cache
        // /system
        logFilePath(Environment.getDataDirectory());
        logFilePath(Environment.getDownloadCacheDirectory());
        logFilePath(Environment.getRootDirectory());

        // /data/user/0/com.demo.threadandcache/cache
        // /data/user/0/com.demo.threadandcache/files
        logFilePath(getCacheDir());
        logFilePath(getFilesDir());

        logFilePath(getDatabasePath("test"));
        ///data/user/0/com.demo.threadandcache/app_test
        logFilePath(getDir("test", Context.MODE_PRIVATE));

        // /data/app/com.demo.threadandcache-Axlv4uVYpywaF83zfiFmEQ==/base.apk
        // /data/app/com.demo.threadandcache-Axlv4uVYpywaF83zfiFmEQ==/base.apk
        Log.d(TAG, getPackageCodePath());
        Log.d(TAG, getPackageResourcePath());
        Log.d(TAG, "----------------------------");

        // /storage/emulated/0
        // /storage/emulated/0/Download
        logFilePath(Environment.getExternalStorageDirectory());
        logFilePath(file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        // /storage/emulated/0/Android/data/com.demo.threadandcache/cache
        // /storage/emulated/0/Android/data/com.demo.threadandcache/files/Pictures
        logFilePath(getExternalCacheDir());
        logFilePath(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        Log.d(TAG, "----------------------------");

        // getFreeSpace():空闲空间
        // getUsableSpace():可用空间
        // getTotalSpace():总容量
        Log.d(TAG, "FreeSpace: " + file.getFreeSpace());
        Log.d(TAG, "TotalSpace: " + file.getTotalSpace());
        Log.d(TAG, "UsableSpace: " + file.getUsableSpace());
        final StatFs stats = new StatFs(file.getPath());
        Log.d(TAG, "UsableSpace: " + (long) stats.getBlockSize() * (long) stats.getAvailableBlocks());
        Log.d(TAG, "allocatableBytes: " + Utils.getAllocatableBytes(file));
        Log.d(TAG, "----------------------------");
    }

    private void logFilePath(File file) {
        if (file != null && file.exists()) {
            Log.d(TAG, file.getAbsolutePath());
        }
    }
}
