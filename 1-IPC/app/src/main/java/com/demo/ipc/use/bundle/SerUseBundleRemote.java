package com.demo.ipc.use.bundle;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.demo.ipc.use.bundle.ActUseBundle.ACTION;

/**
 * @author 尉迟涛
 * create time : 2019/11/13 22:57
 * description :
 */
public class SerUseBundleRemote extends IntentService {

    private static final String TAG = SerUseBundleRemote.class.getSimpleName();

    public SerUseBundleRemote() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int start, end;
        if (intent == null) {
            return;
        }
        start = intent.getIntExtra("start", 0);
        end = intent.getIntExtra("end", 10);

        int progress = start;
        try {
            Thread.sleep(300);
            while (progress <= end) {
                Thread.sleep(300);
                sendThreadStatus(progress++);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送进度消息
     */
    private void sendThreadStatus(int progress) {
        Log.e(TAG, "progress: " + progress);
        Intent intent = new Intent();
        intent.putExtra("progress", progress);
        intent.setAction(ACTION);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "线程结束运行");
    }
}
