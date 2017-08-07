package org.dovakin.push.client.core.service;


/**
 * Created by liuhuanchao on 2017/7/25.
 */

public interface NotifyService {

    /**
     * 当发生推送消息时，客户端会通过此方法通知应用处理回调事件，并将推送消息作为参数传入应用
     * @param type 消息类型
     * @param action 推送消息POJO
     */
    public void notify(int type, Object action);

    public void onActive();

    public void onInactive();

    public void onException(String e);
}
