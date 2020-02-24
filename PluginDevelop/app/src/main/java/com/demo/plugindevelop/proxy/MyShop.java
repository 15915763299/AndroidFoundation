package com.demo.plugindevelop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 尉迟涛
 * create time : 2020/2/21 22:13
 * description : 这是一家小店，提供定制服务，有完好的售前、售后服务
 */
public class MyShop implements InvocationHandler {

    private Object factory;

    public void setFactory(Object factory) {
        this.factory = factory;
    }

    private void preSaleService() {
        System.err.println("售前服务：严选商品，精美包装");
    }

    private void afterSaleService() {
        System.err.println("售后服务：使用指导，退货换货");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        preSaleService();
        Object result = method.invoke(factory, args);
        afterSaleService();
        return result;
    }

    public Object myShopProxy() {
        if (factory == null) {
            return null;
        }
        Class<?> clazz = factory.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);

        // newProxyInstance 参数：
        // loader: 一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
        // interfaces: 一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，
        //       如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，
        //       这样我就能调用这组接口中的方法了
        // h: 一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，
        //       会关联到哪一个InvocationHandler对象上
    }

    // newProxyInstance方法中：
    // 1、例行检查
    // 2、生成动态代理类：Class<?> cl = getProxyClass0(loader, intfs)
    // 3、返回动态代理对象

    // getProxyClass0方法中：
    // return proxyClassCache.get(loader, interfaces);

    // 使用缓存，继续看 get：
    // 看到：Object subKey = Objects.requireNonNull(subKeyFactory.apply(key, parameter));

    // 注意到subKeyFactory，我们找到cache的赋值的地方：
    //    /**
    //     * a cache of proxy classes
    //     */
    //    private static final WeakCache<ClassLoader, Class<?>[], Class<?>>
    //        proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
    // 发现这是一个 ProxyClassFactory
    // 看 apply，调用者实现了BiFunction接口，实际上是ProxyClassFactory

    // 其中 apply的实现如下：
    // public Class<?> apply(ClassLoader var1, Class<?>[] var2) {
    // 参数一个是ClassLoader，一个是接口，就是一开始的入参

    // (Java后台的包有 ProxyGenerator，但是安卓没有)
    // 继续看到 byte[] var22 = ProxyGenerator.generateProxyClass(var23, var2, var17);
    // var22就是产出的动态代理类的字节码
    // 前面两行：
    //   long var19 = nextUniqueNumber.getAndIncrement();
    //   String var23 = var16 + "$Proxy" + var19;
    //   一个是Atom Integer，一个是 $Proxy，组成名字
    // 最后调用 return Proxy.defineClass0(var1, var23, var22, 0, var22.length);
    // 返回类 class

    // 这个类只存在于内存中，不过我们可以取出这段代码，将其写出，然后使用反编译工具编译出来

    // 看到动态代理类的源码后我们发现
    // 一个对象经过动态代理之后，动态代理对象的任何方法被调用，都会调用到 InvocationHandler
    // 实际上就是加一个中间层

    // “计算机科学领域的任何问题都可以通过增加一个间接的中间层来解决”
    //
    // “Any problem in computer science can be solved by anther layer of indirection.”
}
