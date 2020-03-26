package com.demo.ui;

/**
 * @author 尉迟涛
 * create time : 2019/11/24 1:33
 * description :
 */
public class Test {

    public static void main(String[] args) {
        test();
    }

//    /**
//     * MeasureSpec 中的常量值查看
//     */
//    private static void measureSpecTest() {
//        System.out.println(Integer.toBinaryString(30));
//        System.out.println("MODE_MASK--: " + Integer.toBinaryString(0x3 << 30));//等同于3 << 30
//        System.out.println("UNSPECIFIED: " + Integer.toBinaryString(0 << 30) + "0000000000000000000000000000000");
//        System.out.println("EXACTLY----: 0" + Integer.toBinaryString(1 << 30));
//        System.out.println("AT_MOST----: " + Integer.toBinaryString(2 << 30));
//    }

    private static void test(){
        int oldCapacity = 0;
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        System.out.println(Integer.toBinaryString(newCapacity));
    }
}
