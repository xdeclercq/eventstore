package com.autelhome.eventstore.client;

import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;

/**
 * @author xdeclercq
 */
public class MessageDeserializer {


    public <T> T deserialize(final Class<T> eventClass, final byte[] data) {
        final Schema schema = ReflectData.get().getSchema(eventClass);
        final DatumReader<Object> datumReader = new ReflectDatumReader<>(schema);
        try {
            final Decoder decoder = new DecoderFactory().binaryDecoder(data, null);
            return (T) datumReader.read(null, decoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
