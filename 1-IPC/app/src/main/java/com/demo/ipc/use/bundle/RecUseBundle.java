package com.demo.ipc.use.bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * @author 尉迟涛
 * create time : 2019/11/13 23:38
 * description :
 */
public class RecUseBundle extends BroadcastReceiver {

    private WeakReference<ActUseBundle> wr;

    public RecUseBundle(ActUseBundle act) {
        wr = new WeakReference<>(act);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (wr != null && wr.get() != null && intent != null) {
            ActUseBundle act = wr.get();
            int progress = intent.getIntExtra("progress", 0);
            act.showText(String.valueOf(progress));
        }
    }
}
