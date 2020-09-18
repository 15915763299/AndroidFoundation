package com.demo.ipc.use.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author 尉迟涛
 * create time : 2019/11/14 18:41
 * description :
 */
public class SerUseMessengerRemote extends Service {

    private static final String TAG = SerUseMessengerRemote.class.getSimpleName();

    private static class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == ActUseMessenger.MSG_FROM_CLIENT) {
                Log.e(TAG, msg.getData().getString("msg"));

                Messenger clientMessenger = msg.replyTo;
                Message replyMessage = Message.obtain(null, ActUseMessenger.MSG_FROM_SERVICE);

                Bundle bundle = new Bundle();
                bundle.putString("reply", "Hi, this is service");
                replyMessage.setData(bundle);
                try {
                    clientMessenger.send(replyMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    private Messenger serviceMessenger = new Messenger(new ServiceHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }
}
