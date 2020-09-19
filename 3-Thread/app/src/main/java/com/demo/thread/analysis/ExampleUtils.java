package com.demo.thread.analysis;

/**
 * @author 尉迟涛
 * create time : 2020/9/19 11:48
 * description :
 */
public class ExampleUtils {

    public static final String END = "End";

    public static int randomTime(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

}
