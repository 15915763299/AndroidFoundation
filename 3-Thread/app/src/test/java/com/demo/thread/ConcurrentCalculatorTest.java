package com.demo.thread;

import com.demo.thread.analysis.ConcurrentCalculator;

import org.junit.Test;

/**
 * @author 尉迟涛
 * create time : 2020/9/19 10:13
 * description :
 */
public class ConcurrentCalculatorTest {

    @Test
    public void test(){

        int[] numbers = new int[10000];
        for (int i = 0; i < 10000; i++) {
            numbers[i] = i + 1;
        }

        ConcurrentCalculator calc = new ConcurrentCalculator();
        Long sum = calc.sum(numbers);
        System.out.println(sum);
        calc.close();
    }

}
