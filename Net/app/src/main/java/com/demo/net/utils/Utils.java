package com.demo.net.utils;

import java.util.Random;

/**
 * @author 尉迟涛
 * create time : 2020/3/7 9:26
 * description :
 */
public class Utils {

//    public static String getRandomString(int length) {
//        Random random = new Random();
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < length; i++) {
//            int number = random.nextInt(3);
//            long result = 0;
//            switch (number) {
//                case 0:
//                    result = Math.round(Math.random() * 25 + 65);
//                    sb.append((char) result);
//                    break;
//                case 1:
//                    result = Math.round(Math.random() * 25 + 97);
//                    sb.append((char) result);
//                    break;
//                case 2:
//                    sb.append(new Random().nextInt(10));
//                    break;
//            }
//        }
//        return sb.toString();
//    }

    public static String getRandomString(int length) {
//        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }
}
