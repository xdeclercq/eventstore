package com.autelhome.eventstore;

import org.apache.avro.reflect.AvroName;
import org.apache.avro.reflect.AvroSchema;

/**
 * @author xdeclercq
 */
@AvroSchema("{\n" +
        "  \"type\" : \"record\",\n" +
        "  \"name\" : \"EventWrapper\",\n" +
        "  \"namespace\" : \"com.autelhome.eventstore\",\n" +
        "  \"fields\" : [ {\n" +
        "    \"name\" : \"event_type\",\n" +
        "    \"type\" : \"string\"\n" +
        "  }, {\n" +
        "    \"name\" : \"event\",\n" +
        "    \"type\" : \"bytes\"\n" +
        "  }, {\n" +
        "    \"name\" : \"version\",\n" +
        "    \"type\" : \"int\"\n" +
        "  }, {\n" +
        "    \"name\" : \"aggregate_id\",\n" +
        "    \"type\" : \"string\"\n" +
        "  } ]\n" +
        "}")
public class EventWrapper {



    @AvroName("event_type")
    private final String eventType;
    private final Object event;
    private final int version;
    @AvroName("aggregate_id")
    private final String aggregateId;

    public EventWrapper(final String aggregateId, final String eventType, final Object event, final int version) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.event = event;
        this.version = version;
    }

    public String getEventType() {
        return eventType;
    }

    public Object getEvent() {
        return event;
    }

    public int getVersion() {
        return version;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String toString() {
        return "EventWrapper{" +
                "eventType='" + eventType + '\'' +
                ", event=" + event +
                ", version=" + version +
                ", aggregateId='" + aggregateId + '\'' +
                '}';
    }
}
