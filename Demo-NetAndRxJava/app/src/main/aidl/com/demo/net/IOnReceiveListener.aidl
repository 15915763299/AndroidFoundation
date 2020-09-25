// IOnReceiveListener.aidl
package com.demo.net;

// Declare any non-default types here with import statements

interface IOnReceiveListener {

    void onHeartBeat(String msg);

    void onReceiveMsg(String msg);

}
