package com.autelhome.eventstore.cqrs;

import com.autelhome.eventstore.EventStore;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

/**
 * @author xdeclercq
 */
public abstract class AbstractRepository<T extends Aggregate> implements Repository<T> {

    private final EventStore eventStore;

    protected AbstractRepository(final EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void save(final T aggregate, final int expectedVersion) {
        eventStore.saveEvents(aggregate.getId().toString(), aggregate.getUncommittedEvents(), expectedVersion);
        aggregate.clearUncommitedEvents();
    }

    @Override
    public T getById(final EntityId id) {
        final Class<T> aggregateClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        final T aggregate;
        try {
            aggregate = aggregateClass.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new AggregateInstantiationException(id, e);
        }

        final SortedMap<Integer, Object> eventsByVersion = eventStore.getEventsForAggregate(id.toString());

        final List<Event> events = eventsByVersion.entrySet().stream()
                .map(entry -> (Event) entry.getValue())
                .collect(Collectors.toList());

        final int lastVersion = eventsByVersion.lastKey();

        aggregate.loadFromHistory(events, lastVersion);
        return aggregate;
    }
}
