package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface EventHandler<E> {

    /**
     * @param event an event
     * @param version the version of the aggregate right after this event occurred
     * @param isNew indicates if the event has already been sent
     */
    void handle(final E event, final int version, boolean isNew);
}
