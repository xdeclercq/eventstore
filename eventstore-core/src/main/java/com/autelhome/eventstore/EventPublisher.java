package com.autelhome.eventstore;

import java.util.List;

/**
 * @author xdeclercq
 */
public interface EventPublisher {

    void publish(final String aggregateId, final List<? extends Object> events, final int expectedVersion);
}
