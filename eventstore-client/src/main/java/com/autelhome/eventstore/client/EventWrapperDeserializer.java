package com.autelhome.eventstore.client;

import com.autelhome.eventstore.EventWrapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;

/**
 * @author xdeclercq
 */
public class EventWrapperDeserializer {

    private final Schema eventWrapperSchema;
    private final MessageDeserializer messageDeserializer;

    public EventWrapperDeserializer() {
        eventWrapperSchema = ReflectData.get().getSchema(EventWrapper.class);
        messageDeserializer = new MessageDeserializer();
    }

    public EventWrapper deserialize(final byte[] data) {

        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(eventWrapperSchema);
        try {
            final Decoder decoder = new DecoderFactory().binaryDecoder(data, null);
            final GenericRecord genericRecord = datumReader.read(null, decoder);
            return convert(genericRecord);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: manage exception
        }
        return null;
    }

    private EventWrapper convert(final GenericRecord genericRecord) {
        final String eventType = genericRecord.get("event_type").toString();
        final Optional<Class> eventClass = getClassForClassName(eventType);
        final Object event = eventClass.isPresent() ? messageDeserializer.deserialize(eventClass.get(), ((ByteBuffer) genericRecord.get("event")).array()) : null;
        return new EventWrapper(genericRecord.get("aggregate_id").toString(),
                eventType,
                event,
                (Integer) genericRecord.get("version"));
    }

    private Optional<Class> getClassForClassName(final String className) {
        for (final Package p : Package.getPackages()) {
            try {
                return Optional.of(Class.forName(p.getName() + "." + className));
            } catch (ClassNotFoundException e) {
                // do nothing
            }
        }
        return Optional.empty();
    }
}
