package com.demo.thread.thread;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author 尉迟涛
 * create time : 2020/2/1 20:44
 * description :
 */
public class SerIntentService extends IntentService {

    public SerIntentService() {
        super("SerIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            SystemClock.sleep(3000);
            String data = intent.getStringExtra("data");

            Log.d(Thread.currentThread().getName(), data + " done");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand: " + Thread.currentThread().getName());
        System.out.println("onStartCommand: " + (Looper.getMainLooper() == Looper.myLooper()));
        return super.onStartCommand(intent, flags, startId);
    }
}
