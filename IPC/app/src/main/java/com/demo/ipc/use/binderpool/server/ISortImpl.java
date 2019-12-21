package com.demo.ipc.use.binderpool.server;

import android.os.RemoteException;

import com.demo.ipc.binderpool.ISort;

import java.util.Arrays;

/**
 * @author 尉迟涛
 * create time : 2019/11/16 17:44
 * description :
 */
public class ISortImpl extends ISort.Stub {

    @Override
    public String sortChar(String rowStr) throws RemoteException {
        if (rowStr == null || rowStr.equals("")) {
            return "";
        }
        char[] chars = rowStr.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

}
