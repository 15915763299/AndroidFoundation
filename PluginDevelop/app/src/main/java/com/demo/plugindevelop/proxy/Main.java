package com.demo.plugindevelop.proxy;

import com.demo.plugindevelop.proxy.interfaces.CakeFactory;
import com.demo.plugindevelop.proxy.interfaces.ToyFactory;
import com.demo.plugindevelop.proxy.impl.BigFactory;
import com.demo.plugindevelop.proxy.impl.SmallFactory;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 22:38
 * description :
 *
 * 静态代理一句话：实现接口并持有接口的一个实例对象
 * 静态代理只能一对一，动态代理可以一对多
 */
public class Main {

    public static void main(String[] args) {
        // 我的小店
        MyShop myShop = new MyShop();

        // 我能联系到的工厂，可以定制玩具和蛋糕
        BigFactory bigFactory = new BigFactory();
        SmallFactory smallFactory = new SmallFactory();

        // 定制一个玩具
        myShop.setFactory(bigFactory);
        // 通知工厂
        ToyFactory toyFactoryBig = (ToyFactory) myShop.myShopProxy();
        // 售前服务、制造、售后服务（这里实际上是调用了proxy对象）
        String toy1 = toyFactoryBig.makeToy("变形金刚");
        // 看一下玩具
        System.out.println(toy1);
        System.out.println("-------------------");

        // 定制一个蛋糕
        myShop.setFactory(smallFactory);
        CakeFactory toyFactorySmall = (CakeFactory) myShop.myShopProxy();
        String cake1 = toyFactorySmall.makeCake("草莓味");
        System.out.println(cake1);
        System.out.println("-------------------");

        // 当然，我们也可以找大工厂定制一个蛋糕
        myShop.setFactory(bigFactory);
        CakeFactory cakeFactoryBig = (CakeFactory) myShop.myShopProxy();
        String cake2 = cakeFactoryBig.makeCake("芒果味");
        System.out.println(cake2);
        System.out.println("-------------------");
    }

}
