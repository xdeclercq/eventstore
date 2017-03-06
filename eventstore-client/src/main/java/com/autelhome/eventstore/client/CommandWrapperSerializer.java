package com.autelhome.eventstore.client;

import com.autelhome.eventstore.CommandWrapper;
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
public class CommandWrapperSerializer {

    private final Schema commandWrapperSchema;
    private final MessageSerializer messageSerializer;

    public CommandWrapperSerializer() {
        commandWrapperSchema = ReflectData.get().getSchema(CommandWrapper.class);
        this.messageSerializer = new MessageSerializer();
    }

    public byte[] serialize(final CommandWrapper commandWrapper) {
        final ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        final DatumWriter<Object> datumWriter = new GenericDatumWriter(commandWrapperSchema);
        try {
            final Encoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
            GenericRecord genericRecord = convert(commandWrapper, commandWrapperSchema);
            datumWriter.write(genericRecord, encoder);
            encoder.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private GenericRecord convert(final CommandWrapper commandWrapper, final Schema schema) {
        final GenericRecord genericRecord = new GenericData.Record(schema);
        genericRecord.put("command_type", commandWrapper.getCommandType());
        final byte[] eventBytes = messageSerializer.serialize(commandWrapper.getCommand());
        genericRecord.put("command", ByteBuffer.wrap(eventBytes));
        return genericRecord;
    }

}
