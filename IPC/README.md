# IPC-Demo
IPC（Inter-Process Communication）进程间通信，在安卓中主要有以下几种方式：
* Bundle
* 文件共享（File、SharePreference）
* Messenger
* AIDL
* ContentProvider
* Socket（TCP）


方式 | 适用场景 | 缺点 
-----|------|------
Bundle|四大组件之间|只能传输Bundle支持的数据类型
文件共享|无并发、实时性不高|不适用并发
Messenger|低并发、一对多（串行）的即时通讯，无RPC需求，或无需返回结果的RPC需求|不适用高并发，不支持RCP，只能传输Bundle支持的数据类型
AIDL|一对多通讯且有RPC需求|适用稍微复杂，需要处理好线程同步
ContentProvider|一对多的进程间共享数据|可以理解为受约束的AIDL，主要提供数据库操作
Socket| 频繁快速的网络数据交换 |使用稍微复杂，不支持直接的RPC


注：RPC(Remote Procedure Call Protocol)远程过程调用协议

以上方式代码中各有一示例，其中为了深入理解AIDL，增加了手写AIDL与Binder池的例子。
