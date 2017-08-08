# 关于SDK
**Dovakin For Android SDK**，对Android应用与集成Dovakin推送的服务之间的连接调用进行了良好的封装，通过基于[EventBus 3.0](https://github.com/greenrobot/EventBus)的事件总线机制，提供了包括TCP连接状态改变、数据获取等回调事件，无需关注更多细节

## 依赖
目前SDK的最新release版本为v0.3.0，你可以在[这里](https://github.com/dovakinlink/netty-dovakin-android-client/releases/tag/v0.3.0)下载
通过File->Project Structure->Dependencies->Add Jardependency操作来添加SDK依赖

除了SDK本身，你仍然需要在你的build.gradle中添加如下代码，添加[netty](https://github.com/netty/netty),   [gson](https://github.com/google/gson)，
[EventBus3.0](https://github.com/greenrobot/EventBus)的支持
```gradle
    compile 'io.netty:netty-all:4.1.13.Final'
    compile 'com.google.code.gson:gson:2.8.1'
    compile 'org.greenrobot:eventbus:3.0.0'
```
## 如何开始

### <a name="step 1">Step 1:</a> 注册EventBus
你需要将你的Service类注册到EventBus总线上，并通过接收 **BaseEvent** 和 **ChannelEvent** 事件，来编写SDK事件的回调处理
```java
    EventBus.getDefault().register(this);
```

数据接收回调处理:
```java
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleNotify(BaseEvent baseEvent){
        switch (baseEvent.getEventType()){
            case EventType.AUTH:
                // do something
                break;
            case EventType.AUTH_SUCCESS:
                // do something
                break;
            case EventType.AUTH_FAILED:
                // do something
                break;
            case EventType.HEART:
                // do something
                break;
            case EventType.PUSH:
                // do something
                break;
        }
    }
```
TCP连接状态改变回调处理：
```java
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleChannelEvent(ChannelEvent channelEvent){
        String eventType = channelEvent.getEventType();
        if(eventType.equals(EventType.ON_ACTIVE)){
            
        } else if (eventType.equals(EventType.ON_INACTIVE)){
            
        } else if(eventType.equals(EventType.ON_EXCEPTION)){

        }
    }
```
SDK中提供了几种内置事件状态，可以通过比对 EventType 中的静态变量来对事件类型进行判别
**EventType**

type | 类型说明 | 类型
-----|---------|--------
PUSH | 推送 | Integer
AUTH | 鉴权 | Integer
AUTH_SUCCESS | 鉴权成功 | Integer
AUTH_FAILED  | 鉴权失败 | Integer
HEART | 心跳回执 | Integer
ON_ACTIVE | TCP连接建立 | String
ON_INACTIVE | TCP连接断开 | String
ON_EXCEPTION | 服务器异常 | String

### <a name="step 2">Step 2:</a> 启动NGLSClient
**NGLS**是Dovakin内置的一个简易自定义推送协议，它提供了最简单的推送协议功能

NGLSClient的启动过程是异步非阻塞的，所以你可以自由选择合适的时机**在主线程**中进行启动，启动代码很简单，下面是一个完整的例子
```java
client = NGLSClient.init(MainActivity.this)
    .address("192.168.0.1", 9999)
    .setHeartBeatTimeIdle(160);
client.start();
```
NGLSClient中提供了一些链式配置函数

函数名 | 函数作用 | 参数 | 是否必须
------|----------|------|--------
init | 初始化 | 上下文实例 | 是
address | 配置远程服务HOST及PORT | HOST/PORT | 是
setHeartBeatTimeIdle | 设置心跳间隔 | 心跳间隔（单位：秒） | 否 
addListener | 通过添加实现了NotifyService接口的实例来以内置回调的方式监听各类回调事件 | 实现了NotifyService接口的实例 | 否

### <a name="step 3">Step 3:</a> 通过NGLSClient.login()鉴权（注册NGLS连接）
关于鉴权的逻辑，根据集成Dovakin服务的后端逻辑不同而不通，目前仅提供最简单的实现，即明文传输**UserName** **Password**

```java
    AuthAction authAction = new AuthAction();
    authAction.setClientId(/** your username*/);
    authAction.setPassword(/** your password*/);
    try {
        client.login(authAction);
    } catch (AuthParamInvailbleException e) {
        e.printStackTrace();
    } catch (ClientInitFailedException e) {
        e.printStackTrace();
    }
```

