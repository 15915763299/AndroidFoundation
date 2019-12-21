// IBinderPool.aidl
package com.demo.ipc.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {

    IBinder queryBinder(int binderCode);

}
