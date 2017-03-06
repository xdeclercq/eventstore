package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.EventBus;
import com.autelhome.eventstore.EventHandler;
import com.autelhome.eventstore.EventPublisher;
import com.autelhome.eventstore.EventWrapper;
import com.autelhome.eventstore.RegistryEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author xdeclercq
 */
public class KafkaEventBus implements EventBus {

    public static String EVENTS_TOPIC = "events";

    private final Map<Class, List<EventHandler<? extends Object>>> handlers = Collections.synchronizedMap(new HashMap<>());
    private final Properties consumerConfig;
    private final String consumerGroupId;
    private final EventPublisher eventPublisher = new KafkaEventPublisher();

    public KafkaEventBus(final String consumerGroupId) {
        this.consumerGroupId = consumerGroupId;

        consumerConfig = new Properties();
        consumerConfig.put("bootstrap.servers", "127.0.0.1:9092");
        consumerConfig.put("group.id", consumerGroupId);
        consumerConfig.put("auto.offset.reset", "earliest");
    }


    public void connect() {
        KafkaUtils.getEventWrappers(Optional.of(consumerGroupId), eventWrapper -> eventWrapper.getEvent()!=null && handlers.containsKey(eventWrapper.getEvent().getClass()))
                .stream()
                .forEach(this::handleReplay);

        final List<String> topics = Collections.singletonList(EVENTS_TOPIC);
        final ConsumerLoop<String, EventWrapper> consumerLoop = new ConsumerLoop<>(topics,
                consumerConfig,
                new StringDeserializer(),
                new EventWrapperKafkaDeserializer(),
                records -> records.forEach(record -> {
                    final EventWrapper eventWrapper = record.value();
                    handle(eventWrapper, true);
                }));
        new Thread(consumerLoop).start();
    }

    private void handleReplay(final EventWrapper eventWrapper) {
        handle(eventWrapper, false);
    }

    private void handle(final EventWrapper eventWrapper, boolean isNew) {
        final Object event = eventWrapper.getEvent();
        if (event == null) return;
        final List<EventHandler<?>> handlersForType = handlers.getOrDefault(event.getClass(), Collections.emptyList());
        handlersForType.forEach(h -> ((EventHandler<Object>) h).handle(event, eventWrapper.getVersion(), isNew));
    }

    @Override
    public <E> RegistryEntry<E> register(final EventHandler<E> handler) {
        return (type) -> {
            final List<EventHandler<?>> handlersForType = handlers.getOrDefault(type, new ArrayList<>());
            handlersForType.add(handler);
            handlers.putIfAbsent(type, handlersForType);
        };
    }

    @Override
    public void publish(final String aggregateId, final List<?> events, final int expectedVersion) {
        eventPublisher.publish(aggregateId, events, expectedVersion);
    }
}
