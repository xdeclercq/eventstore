package com.autelhome.eventstore.cqrs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xdeclercq
 */
public abstract class AbstractAggregate implements Aggregate {

    private final List<Event> uncommitedEvents = new ArrayList<>();
    protected EntityId id;
    protected int version;

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public List<Event> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommitedEvents);
    }

    @Override
    public void clearUncommitedEvents() {
        uncommitedEvents.clear();
    }

    @Override
    public void applyEvent(final Event event) {
        applyEvent(event, true);
    }

    private void applyEvent(final Event event, final boolean isNew) {
        apply(event);
        if (isNew) {
            uncommitedEvents.add(event);
            version++;
        }
    }

    private void apply(final Event event) {
        // TODO: optimize with cache
        try {
            getClass().getMethod("apply", event.getClass()).invoke(this, event);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // TODO: proper exception handling
            e.printStackTrace();
        }
    }

    @Override
    public void loadFromHistory(final List<Event> history, final int version) {
        if (history.isEmpty()) return;
        history.forEach(event -> applyEvent(event, false));
        this.version = version;
    }
}
