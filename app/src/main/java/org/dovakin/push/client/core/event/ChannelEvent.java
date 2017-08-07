package org.dovakin.push.client.core.event;

/**
 * Created by Link on 2017/8/7.
 */

public class ChannelEvent {
    private String eventType;
    private Object object;

    public ChannelEvent(String eventType, Object object){
        this.eventType = eventType;
        this.object = object;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
