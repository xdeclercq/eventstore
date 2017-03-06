package com.autelhome.eventstore.cqrs;

/**
 * @author xdeclercq
 */
public class AggregateInstantiationException extends RuntimeException {

    public AggregateInstantiationException(final EntityId id, final Throwable cause) {
        super(String.format("Unable to instantiate aggregate ", id), cause);
    }
}
