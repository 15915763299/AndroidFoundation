package com.demo.threadandcache.thread;

import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.threadandcache.R;

/**
 * @author 尉迟涛
 * create time : 2020/3/11 9:34
 * description : 总结：可以考虑把主线程的优先级设为10（thread.setPriority）
 * 对于 Process.setThreadPriority，主线程本身默认就比新线程高，所以没必要设置了
 */
public class ActThreadPriority2 extends AppCompatActivity {

    private static final String TAG = "TestThreadPriority2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_thread_priority2);

        findViewById(R.id.btn1).setOnClickListener(v -> testThreadPriority());
        findViewById(R.id.btn2).setOnClickListener(v -> testThreadPriority2());
    }

    /**
     * MIN_PRIORITY = 1;
     * NORM_PRIORITY = 5;
     * MAX_PRIORITY = 10;
     * <p>
     * 新线程/主线程默认都是 5
     * 查看Log发现，优先级数字越小的，一般越晚结束，越大的越快
     */
    public void testThreadPriority() {
        // 启动30个线程
        for (int i = 0; i < 30; i++) {
            final int ii = i;
            new Thread() {
                @Override
                public void run() {
                    // 设置不同的优先级
                    compute(Thread.currentThread(), ii % 9 + 1);
                }
            }.start();
        }

        Thread thread = Thread.currentThread();

        //TODO 可以尝试为主线程设置更高的优先级来提高主线程的流畅度
        // 提高ui线程优先级会减少计算时间，比如设置成10（默认是5）
        int uiPriority = thread.getPriority();//10;
        long l = compute(thread, uiPriority);
        Log.d("ThreadPriority", "主线程优先级: " + uiPriority + "，耗时：" + l);
    }

    /**
     * 设置线程优先级，然后进行大量的字符拼接计算
     */
    private long compute(Thread thread, int priority) {
        //设置当前线程优先级
//        thread.setPriority(priority);

        long s = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 400000; j++) {
            sb.append(j);
            if (sb.length() > 500) {
                sb = new StringBuilder();
            }
        }
        long spend = (System.currentTimeMillis() - s);

        //priority-优先级, spend-花费时间
        Log.d("ThreadPriority", thread.getName() +
                ": priority-" + thread.getPriority() +
                ", spend-" + spend);

        return spend;
    }


    // *******************************************************************************************

    /**
     * THREAD_PRIORITY_LOWEST = 19;
     * THREAD_PRIORITY_BACKGROUND = 10;
     *
     * THREAD_PRIORITY_DEFAULT = 0;
     *
     * THREAD_PRIORITY_FOREGROUND = -2;
     * THREAD_PRIORITY_DISPLAY = -4;
     * THREAD_PRIORITY_URGENT_DISPLAY = -8;
     * THREAD_PRIORITY_VIDEO = -10;         视频
     * THREAD_PRIORITY_AUDIO = -16;         录音
     * THREAD_PRIORITY_URGENT_AUDIO = -19;  紧急录音
     * <p>
     * 新线程默认 0，主线程默认 -10
     */
    public void testThreadPriority2() {

        for (int i = 0; i < 30; i++) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
//                    Process.setThreadPriority(19);
//                    Thread.currentThread().setPriority(1);
                    compute();
                }
            }.start();
        }

        //Process.setThreadPriority(-19);
        //Thread.currentThread().setPriority(10);
        long l = compute();
        int pPriority = Process.getThreadPriority(Process.myTid());
        Log.d("ThreadPriority", "主线程优先级: " + pPriority + "，耗时：" + l);
    }

    /**
     * 进行大量的字符拼接计算
     */
    private long compute() {
        long s = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 400000; j++) {
            sb.append(j);
            if (sb.length() > 500) {
                sb = new StringBuilder();
            }
        }
        long spend = (System.currentTimeMillis() - s);

        Thread thread = Thread.currentThread();
        //priority-优先级, spend-花费时间
        Log.d("ThreadPriority", thread.getName() +
                ": priority-" + Process.getThreadPriority(Process.myTid()) +
                ", spend-" + spend);

        return spend;
    }


}
