# 网络编程
以下按包路径说明功能。因为网络编程常用到 okhttp 与 RxJava，所以一起写在这里。

com.demo.net
├── chatroom `使用socket实现简易聊天室`
├── okhttp `okhttp代码示例`
├── rxjava
│　　　├── demo `一些操作符的使用方式介绍，以及一些常用Demo`
│　　　├── observer `自定义 & Java原生 & RxJava观察者模式实现`
│　　　├── preload `利用BehaviorSubject特性，自定义预读类`
│　　　├── rxbus `利用热被观察者特性，自定义RxBus，用于事件传递`
│　　　├── ActRxJava `RxJava线程切换与背压问题`
│　　　└── HotColdObservableDemo `冷热被观察者区别`
└── test `Socket、UDP等网络编程相关对象的测试`
