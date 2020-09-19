# AndroidFoundation

## 一、阅读《Android艺术探索》回顾基础知识，运行手机为三星Galaxy Note8，Android版本为9。

### 1、IPC
IPC（Inter-Process Communication）进程间通信简单代码示例，可直接运行App查看。

### 2、UI
自定义View、点击事件、滑动冲突、动画等UI相关的简单示例与参数分析，可直接运行App查看。

### 3、Thread
Android中常见的多线程的使用方式，并对一些常见线程类做了分析。

### 4、ImageAndCache
图片的三级缓存，以及Glide的部分用法。

## 二、一些Demo

### 1、ChangeSkin
三种换肤方案分析，可以结合使用，`AppCompactActivity.vsdx`文件梳理了替换View的逻辑。

### 2、LazyLoad
完全懒加载，Fragment显示时加载资源，隐藏时释放资源，使用分发机制解决嵌套ViewPager的Fragment的懒加载问题，
使用FragmentStatePagerAdapter，适配两种模式（BEHAVIOR_SET_USER_VISIBLE_HINT与BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT）

### 3、Net
网络编程相关，简单的聊天室实现，UDP通讯简单实现，RxJava+OkHttp+Retrofit+DataBinding实现登录注册，
自定义RxBus，十几种RxJava操作符分析，背压、冷热被观察者分析

### 4、PluginDevelop
插件化原理分析，自定义注解实现，动态代理深入分析，使用 Android Studio 自带模拟器运行，适配6.0+

### 5、SimpleMusicPlayer
简易的音乐播放器，展示动态注册BroadcastReceiver与同进程内的Binder通讯

## 三、源码逻辑梳理
framework层部分代码、okhttp、glide等开源框架逻辑梳理。framework 基于Android 10.0.0_r6。
