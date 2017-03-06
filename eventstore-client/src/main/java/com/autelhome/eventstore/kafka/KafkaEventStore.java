package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.CommandBus;
import com.autelhome.eventstore.EventBus;
import com.autelhome.eventstore.EventStore;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author xdeclercq
 */
public class KafkaEventStore implements EventStore {

    private final CommandBus commandBus;
    private final KafkaEventBus eventBus;


    public KafkaEventStore(final String consumerGroupId) {
        commandBus = new KafkaCommandBus(consumerGroupId);
        eventBus = new KafkaEventBus(consumerGroupId);
    }

    @Override
    public void saveEvents(final String aggregateId, final List<? extends Object> events, final int expectedVersion) {
        // TODO: throw concurrency exception if latest version is not expected version
        eventBus.publish(aggregateId, events, expectedVersion);
    }

    @Override
    public SortedMap<Integer, Object> getEventsForAggregate(final String aggregateId) {
        final SortedMap<Integer, Object> result = new TreeMap();
        KafkaUtils.getEventWrappers(Optional.empty(), e -> e.getAggregateId().equals(aggregateId))
                .forEach(eventWrapper -> result.put(eventWrapper.getVersion(), eventWrapper.getEvent()));

        return result;
    }

    @Override
    public CommandBus getCommandBus() {
        return commandBus;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }
}
