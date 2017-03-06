package com.autelhome.eventstore.cqrs;

/**
 * @author xdeclercq
 */
public interface Command {

    EntityId getAggregateId();

    int getOriginalVersion();
}
