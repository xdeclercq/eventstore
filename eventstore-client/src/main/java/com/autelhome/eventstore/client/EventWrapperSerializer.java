package com.autelhome.eventstore.client;

import com.autelhome.eventstore.EventWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;

/**
 * @author xdeclercq
 */
public class EventWrapperSerializer {

    private final Schema eventWrapperSchema;
    private final MessageSerializer messageSerializer;

    public EventWrapperSerializer() {
        eventWrapperSchema = ReflectData.get().getSchema(EventWrapper.class);
        this.messageSerializer = new MessageSerializer();
    }

    public byte[] serialize(final EventWrapper eventWrapper) {
        final ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        final DatumWriter<Object> datumWriter = new GenericDatumWriter(eventWrapperSchema);
        try {
            final Encoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
            GenericRecord genericRecord = convert(eventWrapper, eventWrapperSchema);
            datumWriter.write(genericRecord, encoder);
            encoder.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private GenericRecord convert(final EventWrapper eventWrapper, final Schema schema) {
        final GenericRecord genericRecord = new GenericData.Record(schema);
        genericRecord.put("event_type", eventWrapper.getEventType());
        genericRecord.put("aggregate_id", eventWrapper.getAggregateId());
        genericRecord.put("version", eventWrapper.getVersion());
        final byte[] eventBytes = messageSerializer.serialize(eventWrapper.getEvent());
        genericRecord.put("event", ByteBuffer.wrap(eventBytes));
        return genericRecord;
    }

}
