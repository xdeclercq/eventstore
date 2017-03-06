package com.autelhome.eventstore.cqrs;

/**
 * @author xdeclercq
 */
public class AbstractCommand implements Command {

    protected final EntityId aggregateId;
    protected final int originalVersion;

    protected AbstractCommand(final EntityId aggregateId, final int originalVersion) {
        this.aggregateId = aggregateId;
        this.originalVersion = originalVersion;
    }

    @Override
    public EntityId getAggregateId() {
        return aggregateId;
    }

    @Override
    public int getOriginalVersion() {
        return originalVersion;
    }
}
