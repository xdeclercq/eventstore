package com.autelhome.eventstore;

import java.util.List;
import java.util.SortedMap;

/**
 * @author xdeclercq
 */
public interface EventStore {

    void saveEvents(final String aggregateId, final List<? extends Object> events, final int expectedVersion);

    SortedMap<Integer, Object> getEventsForAggregate(final String aggregateId);

    CommandBus getCommandBus();

    EventBus getEventBus();

}
