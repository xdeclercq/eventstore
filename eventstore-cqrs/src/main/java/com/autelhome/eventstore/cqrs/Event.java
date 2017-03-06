package com.autelhome.eventstore.cqrs;

/**
 * @author xdeclercq
 */
public interface Event {

    EntityId getAggregateId();
}
