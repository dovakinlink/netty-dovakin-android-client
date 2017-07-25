package org.dovakin.push.client.core.bean;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class PushAction {

    private int notifyCount;
    private String pushTitle;
    private String pushMessage;

    public int getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }
}
