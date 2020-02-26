package com.demo.net.test;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author 尉迟涛
 * create time : 2020/2/25 0:10
 * description :
 */
public class URLTest {

    public static void main(String[] args) throws Exception {
        URL base = new URL("https://www.baidu.com");
        URL url = new URL(base, "/s?ie=UTF-8&wd=Android");

        System.out.println(url.getProtocol());
        System.out.println(url.getHost());
        //端口默认 -1
        System.out.println(url.getPort());
        System.out.println(url.getPath());
        System.out.println(url.getRef());
        System.out.println(url.getQuery());

        System.out.println("--------------------------------------------");

        // 字节流
        InputStream is = null;
        // 字符流
        InputStreamReader isr = null;
        // 缓冲
        BufferedReader br = null;
        try {
            is = url.openStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String data;
            while ((data = br.readLine()) != null) {
                System.out.println(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(br);
            close(isr);
            close(is);
        }

    }

    private static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
