package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface EventBus extends EventHandlerRegistry, EventPublisher {

    public static String EVENTS_TOPIC = "events";

    void connect();
}
