// IEncrypt.aidl
package com.demo.ipc.binderpool;

// Declare any non-default types here with import statements

interface IEncrypt {

    String encryptSha256(String rowStr);

}
