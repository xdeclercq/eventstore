package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.CommandWrapper;
import com.autelhome.eventstore.client.CommandWrapperDeserializer;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author xdeclercq
 */
public class CommandWrapperKafkaDeserializer implements Deserializer<CommandWrapper> {

    private final CommandWrapperDeserializer commandWrapperDeserializer = new CommandWrapperDeserializer();

    @Override
    public void configure(final Map configs, final boolean isKey) {

    }

    @Override
    public CommandWrapper deserialize(final String topic, final byte[] data) {
        return commandWrapperDeserializer.deserialize(data);
    }

    @Override
    public void close() {

    }
}
