package org.dovakin.push.client.core.event;

/**
 * Created by Link on 2017/8/7.
 */

public class ChannelEvent {
    private String eventType;
    private String object;

    public ChannelEvent(String eventType, String object){
        this.eventType = eventType;
        this.object = object;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
