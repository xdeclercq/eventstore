package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.CommandBus;
import com.autelhome.eventstore.CommandHandler;
import com.autelhome.eventstore.CommandWrapper;
import com.autelhome.eventstore.RegistryEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author xdeclercq
 */
public class KafkaCommandBus implements CommandBus {

    public static String COMMANDS_TOPIC = "commands";

    private final Map<Class, CommandHandler> handlers = Collections.synchronizedMap(new HashMap<>());

    final Producer producer;
    private final Properties consumerConfig;

    public KafkaCommandBus(final String consumerGroupId) {
        final Properties producerConfig = new Properties();
        producerConfig.put("bootstrap.servers", "127.0.0.1:9092");
        producer = new KafkaProducer(producerConfig, new StringSerializer(), new CommandWrapperKafkaSerializer());

        consumerConfig = new Properties();
        consumerConfig.put("bootstrap.servers", "127.0.0.1:9092");
        consumerConfig.put("group.id", consumerGroupId);
        consumerConfig.put("auto.offset.reset", "earliest");
    }

    @Override
    public void send(final String aggregateId, final Object command) {
        System.out.println("send command " + command);

        final CommandWrapper commandWrapper = new CommandWrapper(command.getClass().getSimpleName(), command);
        producer.send(new ProducerRecord(COMMANDS_TOPIC, aggregateId, commandWrapper));
    }

    public void connect() {
        final List<String> topics = Arrays.asList(COMMANDS_TOPIC);

        final ConsumerLoop<String, CommandWrapper> consumerLoop = new ConsumerLoop<>(topics,
                consumerConfig,
                new StringDeserializer(),
                new CommandWrapperKafkaDeserializer(),
                records -> records.forEach(record -> {
            final CommandWrapper commandWrapper = record.value();
            if (commandWrapper.getCommand()==null) return;
            final CommandHandler handler = handlers.get(commandWrapper.getCommand().getClass());
            if (handler!=null) handler.handle(commandWrapper.getCommand());
        }));
        new Thread(consumerLoop).start();
    }

    @Override
    public <C> RegistryEntry<C> register(final CommandHandler<C> handler) {
        return (type) -> {
            // TODO create specific exception
            if (handlers.containsKey(type)) throw new RuntimeException("A command handler has already been registered for the comand type " + type.getCanonicalName());
            handlers.put(type, handler);
        };
    }
}
