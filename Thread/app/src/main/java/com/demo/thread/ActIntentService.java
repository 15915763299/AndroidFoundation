package com.demo.thread;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2020/2/1 17:51
 * description :
 */
public class ActIntentService extends Activity implements View.OnClickListener {

    private class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(Thread.currentThread().getName(), msg.obj + " done");
        }
    }

    private HandlerThread handlerThread;
    private MyHandler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intent_service);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);

        handlerThread = new HandlerThread("HandlerThread");
        // 如果执行start方法，则会在主线程中重新创建一个新的线程
        // 等得到cpu的时间段后则会执行所对应的run方法体的代码。
        // 这里不能直接执行run，不然就是在主线程里执行了，会在主线程里创建Looper，然后报错
        handlerThread.start();
        myHandler = new MyHandler(handlerThread.getLooper());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                // 执行以下语句会
                Message msg = myHandler.obtainMessage(1, "test");
                myHandler.sendMessage(msg);
                break;
            case R.id.btn2:
                Intent intent = new Intent(this, SerIntentService.class);
                intent.putExtra("data", "test");
                startService(intent);
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        handlerThread.quitSafely();
        super.onDestroy();
    }
}
