package com.demo.ipc.use.binderpool.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.demo.ipc.binderpool.IBinderPool;

import static com.demo.ipc.use.binderpool.BinderConstant.BINDER_ENCRYPT;
import static com.demo.ipc.use.binderpool.BinderConstant.BINDER_SORT;

/**
 * @author 尉迟涛
 * create time : 2019/11/16 17:18
 * description :
 */
public class SerBinderPool extends Service {

    private BinderPoolImpl binderPool = new BinderPoolImpl();

    private static class BinderPoolImpl extends IBinderPool.Stub {

        BinderPoolImpl() {
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_ENCRYPT: {
                    binder = new IEncryptImpl();
                    break;
                }
                case BINDER_SORT: {
                    binder = new ISortImpl();
                    break;
                }
                default:
                    break;
            }
            return binder;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }
}
