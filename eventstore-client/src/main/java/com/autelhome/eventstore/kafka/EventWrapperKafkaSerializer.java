package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.EventWrapper;
import com.autelhome.eventstore.client.EventWrapperSerializer;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @author xdeclercq
 */
public class EventWrapperKafkaSerializer implements Serializer<EventWrapper> {

    private final EventWrapperSerializer eventWrapperSerializer = new EventWrapperSerializer();

    @Override
    public void configure(final Map<String, ?> map, final boolean b) {
    }

    @Override
    public byte[] serialize(final String topic, final EventWrapper eventWrapper) {
        return eventWrapperSerializer.serialize(eventWrapper);
    }

    @Override
    public void close() {

    }
}
