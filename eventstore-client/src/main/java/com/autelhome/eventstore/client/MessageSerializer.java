package com.autelhome.eventstore.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;

/**
 * @author xdeclercq
 */
public class MessageSerializer {


    public byte[] serialize(final Object event) {
        final ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        final Schema schema = ReflectData.get().getSchema(event.getClass());
        final DatumWriter<Object> datumWriter = new ReflectDatumWriter(schema);
        try {
            final Encoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
            datumWriter.write(event, encoder);
            encoder.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // TODO: manage exception
            e.printStackTrace();
        }
        return new byte[0];
    }
}
