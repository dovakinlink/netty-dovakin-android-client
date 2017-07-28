package org.dovakin.push.client.core.service;

import org.dovakin.push.client.core.bean.PushAction;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public interface NotifyService {

    /**
     * 当发生推送消息时，客户端会通过此方法通知应用处理回调事件，并将推送消息作为参数传入应用
     * @param action 推送消息POJO
     */
    public void notify(PushAction action);

    public void onActive();

    public void onInactive();

    public void onNGLSActive();

    public void onException(String e);
}
