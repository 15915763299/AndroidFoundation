package com.demo.thread;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

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
            Log.d(Thread.currentThread().getName(), intent.getStringExtra("data") + " done");
        }
    }
}
