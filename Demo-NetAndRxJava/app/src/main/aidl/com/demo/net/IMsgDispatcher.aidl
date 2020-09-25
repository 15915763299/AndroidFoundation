// IMsgDispatcher.aidl
package com.demo.net;

import com.demo.net.IOnReceiveListener;

interface IMsgDispatcher {

     void sendMsg(String msg);

     void registerListener(IOnReceiveListener listener);

}
