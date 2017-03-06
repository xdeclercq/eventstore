package com.autelhome.eventstore.cqrs;

/**
 * @author xdeclercq
 */
public abstract class AbstractCreateCommand extends AbstractCommand {

    protected AbstractCreateCommand(final EntityId aggregateId) {
        super(aggregateId, -1);
    }
}
