package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface CommandBus extends CommandHandlerRegistry {

    void send(final String aggregateId, final Object command);

    void connect();

}
