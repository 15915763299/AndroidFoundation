package com.demo.ipc.use.binderpool.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.demo.ipc.binderpool.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * @author 尉迟涛
 * create time : 2019/11/16 18:18
 * description :
 */
public class BinderPool {

    private static final String TAG = BinderPool.class.getSimpleName();
    private static volatile BinderPool instance;
    private IBinderPool iBinderPool;
    private CountDownLatch countDownLatch;
    private Context context;

    private BinderPool(Context context) {
        this.context = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    private synchronized void connectBinderPoolService() {
        countDownLatch = new CountDownLatch(1);
        Intent service = new Intent("android.intent.action.BINDERPOOL");
        service.setPackage(context.getPackageName());
        context.bindService(service, connection, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * query binder by binderCode from binder pool
     *
     * @param binderCode the unique token of binder
     * @return binder who's token is binderCode<br>
     * return null when not found or BinderPoolService died.
     */
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (iBinderPool != null) {
                binder = iBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                iBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w(TAG, "binder died.");
            iBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            iBinderPool = null;
            connectBinderPoolService();
        }
    };

    public void destroy() {
        context.unbindService(connection);
    }
}
