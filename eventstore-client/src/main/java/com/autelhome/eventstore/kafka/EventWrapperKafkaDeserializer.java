package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.EventWrapper;
import com.autelhome.eventstore.client.EventWrapperDeserializer;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author xdeclercq
 */
public class EventWrapperKafkaDeserializer implements Deserializer<EventWrapper> {

    private final EventWrapperDeserializer eventWrapperDeserializer = new EventWrapperDeserializer();

    @Override
    public void configure(final Map configs, final boolean isKey) {

    }

    @Override
    public EventWrapper deserialize(final String topic, final byte[] data) {
        return eventWrapperDeserializer.deserialize(data);
    }

    @Override
    public void close() {

    }
}
