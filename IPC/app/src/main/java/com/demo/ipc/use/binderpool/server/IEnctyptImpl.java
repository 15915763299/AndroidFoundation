package com.demo.ipc.use.binderpool.server;

import android.os.RemoteException;

import com.demo.ipc.binderpool.IEncrypt;
import com.google.common.hash.Hashing;

/**
 * @author 尉迟涛
 * create time : 2019/11/16 17:52
 * description :
 */
public class IEnctyptImpl extends IEncrypt.Stub {

    @Override
    public String encryptSha256(String rowStr) throws RemoteException {
        if (rowStr == null || rowStr.equals("")) {
            return "";
        }
        return Hashing.sha256().hashBytes(rowStr.getBytes()).toString();
    }
}
