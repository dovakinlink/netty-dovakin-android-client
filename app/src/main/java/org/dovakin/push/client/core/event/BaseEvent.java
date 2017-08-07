package org.dovakin.push.client.core.event;

/**
 * Created by Link on 2017/8/7.
 */

public class BaseEvent {
    private int eventType;
    private Object object;

    public BaseEvent(int eventType, Object object){
        this.eventType = eventType;
        this.object = object;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
