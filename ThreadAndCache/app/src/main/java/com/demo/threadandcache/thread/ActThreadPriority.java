package com.demo.threadandcache.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.threadandcache.R;

/**
 * @author 尉迟涛
 * create time : 2020/3/11 9:34
 * description : Process.setThreadPriority 效果比 thread.setPriority 效果明显
 */
public class ActThreadPriority extends AppCompatActivity {

    private static final String TAG = "TestThreadPriority";

    private Handler handler;
    private TextView tx;
    private StringBuilder sb;
    private MyThread a, b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_thread_priority);

        a = new MyThread("Thread A");
        a.setOSPriority(android.os.Process.THREAD_PRIORITY_LOWEST); // 19
//        a.setPriority(Thread.MAX_PRIORITY); // 10

        b = new MyThread("Thread B");
        b.setOSPriority(Process.THREAD_PRIORITY_URGENT_AUDIO); // -19
//        b.setPriority(Thread.MIN_PRIORITY); // 1

        findViewById(R.id.btn1).setOnClickListener(v -> {
            show("start");
            a.start();
            b.start();
        });

        findViewById(R.id.btn2).setOnClickListener(v -> {
            a.interrupt();
            b.interrupt();
            show("end");
        });

        tx = findViewById(R.id.tx);
        sb = new StringBuilder();
        handler = new Handler(getMainLooper());
    }

    private void show(String info) {
        sb.append(info).append("\n");
        tx.setText(sb.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        a.interrupt();
        b.interrupt();
    }

    private class MyThread extends Thread {
        private int mOSPriority = Process.THREAD_PRIORITY_DEFAULT;
        private int mLoopCount = 0;

        MyThread(String threadName) {
            super(threadName);
        }

        void setOSPriority(int p) {
            mOSPriority = p;
        }

        @Override
        public void run() {
            Process.setThreadPriority(mOSPriority);

            while (!isInterrupted()) {
                mLoopCount++;
                Math.log(Math.random() * 1000); // calculation test
//                Log.d(TAG, getName() +
//                        " os priority: " + mOSPriority +
//                        " java priority: " + getPriority() +
//                        " loop count: " + mLoopCount);
            }

            String end = getName() +
                    " exiting..." + " os priority: " + mOSPriority +
                    " java priority: " + getPriority() +
                    " loop count: " + mLoopCount;
            Log.d(TAG, end);

            handler.post(() -> show(end));
        }
    }

}
