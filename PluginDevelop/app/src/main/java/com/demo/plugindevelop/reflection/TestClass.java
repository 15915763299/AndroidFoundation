package com.demo.plugindevelop.reflection;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 17:25
 * description :
 */
public class TestClass {

    private String value;

    public TestClass() {
    }

    public TestClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "--- " + value + " ---";
    }

    public static void print(String info) {
        System.out.println(info);
    }
}
