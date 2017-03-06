package com.autelhome.eventstore.client;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * @author xdeclercq
 */
public interface ConsumerRecordsHandler<K, V> {

    void handle(final ConsumerRecords<K, V> consumerRecords);
}
