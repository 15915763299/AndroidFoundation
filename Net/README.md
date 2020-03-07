# 网络编程
以下按包路径说明功能。因为网络编程常用到 okhttp 与 RxJava，所以一起写在这里。

com.demo.net
├── chatroom `使用socket实现简易聊天室`
├── okhttp `okhttp代码示例`
├── requestdemo `DataBinding + RxJava + Retrofit + OkHttp 的一个示例`
│　　　├── api `网络请求接口`
│　　　├── bean `实体类`
│　　　├── login `登录界面，实时观察编辑框，点击防抖，链式请求，`
│　　　├── project `project信息获取，实现网络异常重试`
│　　　├── register `注册界面，类似登录界面，注释主要在这里面`
│　　　└── HotColdObservableDemo `冷热被观察者区别`
├── rxjava
│　　　├── demo `一些操作符的使用方式介绍，以及一些常用Demo`
│　　　├── observer `自定义 & Java原生 & RxJava观察者模式实现`
│　　　├── preload `利用BehaviorSubject特性，自定义预读类`
│　　　├── rxbus `利用热被观察者特性，自定义RxBus，用于事件传递`
│　　　├── ActRxJava `RxJava线程切换与背压问题`
│　　　└── HotColdObservableDemo `冷热被观察者区别`
│── test `Socket、UDP等网络编程相关对象的测试`
└── utils `工具`

### DataBinding

DataBinding 是谷歌官方发布的一个框架，顾名思义即为数据绑定，是 MVVM 模式在 Android 上的一种实现，用于降低布局和逻辑的耦合性，使代码逻辑更加清晰。MVVM 相对于 MVP，其实就是将 Presenter 层替换成了 ViewModel 层。DataBinding 能够省去我们一直以来的 findViewById() 步骤，大量减少 Activity 内的代码，数据能够单向或双向绑定到 layout 文件中，有助于防止内存泄漏，而且能自动进行空检测以避免空指针异常
[参考](https://www.jianshu.com/p/bd9016418af2)。
