package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.EventBus;
import com.autelhome.eventstore.EventPublisher;
import com.autelhome.eventstore.EventWrapper;
import com.autelhome.eventstore.kafka.EventWrapperKafkaSerializer;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author xdeclercq
 */
public class KafkaEventPublisher implements EventPublisher {


    private final Producer producer;

    public KafkaEventPublisher() {
        final HashMap<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.servers", "127.0.0.1:9092");

        producer = new KafkaProducer(kafkaConfig, new StringSerializer(), new EventWrapperKafkaSerializer());
    }

    public void publish(final String aggregateId, final List<? extends Object> events, final int expectedVersion) {
        System.out.println("save events for aggregate " + aggregateId);

        IntStream.range(0, events.size())
                .mapToObj(i -> {
                    final Object event = events.get(i);
                    final int version = expectedVersion + i + 1;
                    return new EventWrapper(aggregateId, event.getClass().getSimpleName(), event, version);
                })
                .forEach(e -> producer.send(new ProducerRecord(EventBus.EVENTS_TOPIC, aggregateId, e)));
    }
}
