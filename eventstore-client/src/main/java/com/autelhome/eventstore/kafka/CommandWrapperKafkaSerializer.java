package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.CommandWrapper;
import com.autelhome.eventstore.client.CommandWrapperSerializer;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @author xdeclercq
 */
public class CommandWrapperKafkaSerializer implements Serializer<CommandWrapper> {

    private final CommandWrapperSerializer commandWrapperSerializer = new CommandWrapperSerializer();

    @Override
    public void configure(final Map<String, ?> map, final boolean b) {
    }

    @Override
    public byte[] serialize(final String topic, final CommandWrapper commandWrapper) {
        return commandWrapperSerializer.serialize(commandWrapper);
    }

    @Override
    public void close() {

    }
}
