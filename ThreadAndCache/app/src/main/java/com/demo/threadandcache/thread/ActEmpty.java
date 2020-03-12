package com.demo.threadandcache.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.threadandcache.R;

/**
 * @author 尉迟涛
 * create time : 2020/3/12 11:36
 * description : Android Profiler 工具使用测试，检测内存泄漏，制造一个空Activity
 */
public class ActEmpty extends AppCompatActivity {

    /**
     * 对比有无static的情况
     *
     * 首先，反复打开关闭页面5次，然后收到GC（点击Profile MEMORY左上角的垃圾桶图标，
     * 快速点三下，只点一下你会发现还有很多系统参数引用这Activity，甚至有WeakReference，说明还没被回收），
     * 如果此时total内存还没有恢复到之前的数值，则可能发生了内存泄露。
     * 此时，再点击Profile MEMORY左上角的垃圾桶图标旁的heap dump按钮查看当前的内存堆栈情况，
     * 选择按包名查找，如果退出了当前Activity，但是Activity还存在，则表明发生了内存泄露。
     */
    private /*static*/ class LeakHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private LeakHandler leakHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_empty);

        leakHandler = new LeakHandler();
        leakHandler.postDelayed(() -> {
        }, 30_000);
    }
}
