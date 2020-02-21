//package com.demo.plugindevelop.proxy;
//
//import sun.misc.ProxyGenerator;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * 安卓的Proxy类有改动，需要在后台项目里运行
// *                 // Android-changed: Generate the proxy directly instead of calling
// *                 // through to ProxyGenerator.
// * 文件已生成，请看app目录下的proxyclass文件夹里的 $Proxy0.class, $Proxy1.class
// */
//public class ProxyUtils {
//
//    public static void generateClassFile(Class clazz,String proxyName){
//        /*ProxyGenerator.generateProxyClass(
//                proxyName, interfaces, accessFlags);*/
//        byte[] proxyClassFile =ProxyGenerator.generateProxyClass(
//                proxyName, new Class[]{clazz});
//        String paths = clazz.getResource(".").getPath();
//        System.out.println(paths);
//        FileOutputStream out = null;
//
//        try {
//            out = new FileOutputStream(paths+proxyName+".class");
//            out.write(proxyClassFile);
//            out.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
