package com.demo.plugindevelop.proxy.impl;

import com.demo.plugindevelop.proxy.interfaces.CakeFactory;
import com.demo.plugindevelop.proxy.interfaces.ToyFactory;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 22:30
 * description :
 */
public class BigFactory implements ToyFactory, CakeFactory {

    @Override
    public String makeCake(String name) {
        String product = name + "蛋糕";
        System.out.println("制造了一个" + product);
        return mark(product);
    }

    @Override
    public String makeToy(String name) {
        String product = name + "玩具";
        System.out.println("制造了一个" + product);
        return mark(product);
    }

    private String mark(String product) {
        return product + " - Made in Big Factory";
    }
}
