package com.autelhome.eventstore.cqrs;

import java.util.List;

/**
 * @author xdeclercq
 */
public interface Aggregate {

    List<Event> getUncommittedEvents();

    void clearUncommitedEvents();

    EntityId getId();

    int getVersion();

    void applyEvent(final Event event);

    void loadFromHistory(final List<Event> history, final int version);

}
