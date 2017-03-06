package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.client.ConsumerRecordsHandler;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author xdeclercq
 */
public class ConsumerLoop<K, V>  implements Runnable {
    private final Consumer<K, V> consumer;
    private final List<String> topics;
    private final ConsumerRecordsHandler<K, V> consumerRecordsHandler;

    public ConsumerLoop(final List<String> topics,
                        final Properties props,
                        final Deserializer<K> keyDeserializer,
                        final Deserializer<V> valueDeserilizer,
                        final ConsumerRecordsHandler<K, V> consumerRecordsHandler) {
        this.topics = topics;
        consumer = new KafkaConsumer<>(props, keyDeserializer, valueDeserilizer);
        this.consumerRecordsHandler = consumerRecordsHandler;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            while (true) {
                consumerRecordsHandler.handle(consumer.poll(Long.MAX_VALUE));
            }
        } catch (final WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
