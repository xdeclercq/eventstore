package com.autelhome.eventstore.kafka;

import com.autelhome.eventstore.EventBus;
import com.autelhome.eventstore.EventWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author xdeclercq
 */
public class KafkaUtils {

    /**
     * Get the event wrappers from Kafka. It returns all event wrappers from beginning.
     * If a consumer group is provided:
     *   - then it stops to the last commited offsets
     *   - if there is no commited offset then it returns an empty list.
     * If no consumer group is provided, it stop at the last offset.
     *
     * A filter can be provided to discard some event wrappers.
     *
     * @param consumerGroupId an optional consumer group.
     * @param filter a filter
     * @return the list of event wrappers
     */
    public static List<EventWrapper> getEventWrappers(final Optional<String> consumerGroupId, final Predicate<EventWrapper> filter) {

        final List<EventWrapper> result = new ArrayList<>();
        Map<Integer, Long> endOffsets = getEndOffsets(consumerGroupId);

        final Properties consumerConfig = new Properties();
        consumerConfig.put("bootstrap.servers", "127.0.0.1:9092");
        consumerConfig.put("group.id", UUID.randomUUID().toString());
        consumerConfig.put("auto.offset.reset", "earliest");
        final Consumer<String, EventWrapper> consumer = new KafkaConsumer(consumerConfig, new StringDeserializer(), new EventWrapperKafkaDeserializer());

        try {
            consumer.subscribe(Collections.singletonList(EventBus.EVENTS_TOPIC));
            final Map<Integer, Long> currentOffsets = new HashMap<>();
            endOffsets.entrySet().stream()
                    .forEach(e -> currentOffsets.put(e.getKey(), -1L));

            while (hasAtLeastOneCurrentOffsetNotAtTheEnd(currentOffsets, endOffsets)) {
                final ConsumerRecords<String, EventWrapper> records = consumer.poll(100);
                for (ConsumerRecord<String, EventWrapper> record : records) {
                    final long offset = record.offset();
                    currentOffsets.put(record.partition(), offset);
                    final long endOffset = endOffsets.get(record.partition());
                    if (offset > endOffset) continue;
                    final EventWrapper eventWrapper = record.value();
                    if (filter.test(eventWrapper)) result.add(eventWrapper);
                }
            }
        } finally {
            consumer.close();
        }
        return result;
    }

    private static Map<Integer, Long> getEndOffsets(final Optional<String> consumerGroupId) {

        final Properties consumerConfig = new Properties();
        consumerConfig.put("bootstrap.servers", "127.0.0.1:9092");
        consumerConfig.put("group.id", consumerGroupId.orElse(UUID.randomUUID().toString()));
        consumerConfig.put("auto.offset.reset", "earliest");
        final Consumer<String, EventWrapper> consumer = new KafkaConsumer(consumerConfig, new StringDeserializer(), new EventWrapperKafkaDeserializer());
        final List<PartitionInfo> partitionInfos = consumer.partitionsFor(EventBus.EVENTS_TOPIC);
        final List<TopicPartition> topicPartitions = partitionInfos.stream().map(p -> new TopicPartition(p.topic(), p.partition())).collect(Collectors.toList());

        final Map<Integer, Long> endOffsets = new HashMap<>();
        if (consumerGroupId.isPresent()) {
            topicPartitions.forEach(topicPartition -> {
                final OffsetAndMetadata committed = consumer.committed(topicPartition);
                endOffsets.put(topicPartition.partition(), committed==null ? -1L : committed.offset() - 1);
            });

        } else {
            final Map<TopicPartition, Long> endOffsetsByTopicPartition = consumer.endOffsets(topicPartitions);
            endOffsetsByTopicPartition.entrySet().stream()
                    .forEach(e -> endOffsets.put(e.getKey().partition(), e.getValue() - 1));
        }
        consumer.close();
        return endOffsets;
    }


    private static boolean hasAtLeastOneCurrentOffsetNotAtTheEnd(final Map<Integer, Long> currentOffsets, final Map<Integer, Long> endOffsets) {
        for (Map.Entry<Integer, Long> entry : currentOffsets.entrySet()) {
            final int partition = entry.getKey();
            final Long currentOffset = entry.getValue();
            final Long endOffset = endOffsets.get(partition);
            if (currentOffset<endOffset) return true;
        }
        return false;
    }
}
