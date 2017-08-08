# 关于SDK
**Dovakin For Android SDK**，对Android应用与集成Dovakin推送的服务之间的连接调用进行了良好的封装，通过基于[EventBus 3.0](https://github.com/greenrobot/EventBus)的事件总线机制，提供了包括TCP连接状态改变、数据获取等回调事件，无需关注更多细节

## 依赖
除了SDK本身，你仍然需要在你的build.gradle中添加如下代码，添加[netty](https://github.com/netty/netty),   [gson](https://github.com/google/gson)，
[EventBus3.0](https://github.com/greenrobot/EventBus)的支持
```
    compile 'io.netty:netty-all:4.1.13.Final'
    compile 'com.google.code.gson:gson:2.8.1'
    compile 'org.greenrobot:eventbus:3.0.0'
```
## 如何开始

### <a name="step 1">Step 1:</a> 注册EventBus
你需要将你的Service类注册到EventBus总线上，并通过接收 **BaseEvent** 和 **ChannelEvent** 事件，来编写SDK事件的回调处理
```
    EventBus.getDefault().register(this);
```

数据接收回调处理:
```
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
```
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleChannelEvent(ChannelEvent channelEvent){
        String eventType = channelEvent.getEventType();
        if(eventType.equals(EventType.ON_ACTIVE)){
            
        } else if (eventType.equals(EventType.ON_INACTIVE)){
            
        } else if(eventType.equals(EventType.ON_EXCEPTION)){

        }
    }
```
