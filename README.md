# AndroidFoundation
Android基础知识回顾，以及必要知识记录，运行手机为三星Galaxy Note8，Android版本为9。
### 1、IPC
IPC（Inter-Process Communication）进程间通信简单代码示例，部分截图：
> <img src="/0-Picture/IPC.jpg" alt="部分截图" width="500" height="313" align="center" />

### 2、UI
自定义View、点击事件、滑动冲突、动画等UI相关的简单示例与参数分析，部分截图：
> <img src="/0-Picture/UI.jpg" alt="部分截图" width="500" height="313" align="center" />

### 3、ThreadAndCache
Android中常见的多线程的使用方式、缓存的使用，并对一些常见线程类做了分析，比如：
> 新建一个 ThreadPoolExecutor 的线程池\
> 核心线程2个，最大线程4个，阻塞队列长度为2\
> 瞬间运行 10 个任务，每个执行时长都超过这个“瞬间”的时长\
> 前4个正常运行，阻塞2个，剩下4个被拒绝\
> 你会观察到会有2个被阻塞的任务的log稍晚于前面8个任务的log打出

### 4、PluginDevelop
插件化原理分析，自定义注解实现，动态代理深入分析，使用 Android Studio 自带模拟器运行，适配6.0+

### 5、MusicPlayer
简易的音乐播放器，展示动态注册BroadcastReceiver与同进程内的Binder通讯

### 6、LazyLoad
真正的懒加载，只加载当前显示的Fragment，Fragment移出则立即进行资源释放，解决嵌套ViewPager的Fragment的懒加载问题，
使用FragmentStatePagerAdapter，适配两种模式（BEHAVIOR_SET_USER_VISIBLE_HINT与BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT）

### 8、Net
网络编程相关，简单的聊天室实现

### 7、源码逻辑梳理
framework层部分代码、okhttp、glide等开源框架逻辑梳理。framework 基于Android 10.0.0_r6，下面是 Activity 逻辑分析截图
> ![UI](/0-Picture/Activity.png "Activity逻辑梳理截图")
