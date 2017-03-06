package com.autelhome.eventstore.cqrs;

import org.apache.avro.reflect.AvroName;
import org.apache.avro.reflect.Stringable;

/**
 * @author xdeclercq
 */
public class AbstractEvent implements Event {

    @AvroName("id")
    @Stringable
    protected final EntityId aggregateId;

    protected AbstractEvent(final EntityId aggregateId) {
        this.aggregateId = aggregateId;
    }

    @Override
    public EntityId getAggregateId() {
        return aggregateId;
    }

}
