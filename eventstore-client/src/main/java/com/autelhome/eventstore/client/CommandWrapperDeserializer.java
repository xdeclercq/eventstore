package com.autelhome.eventstore.client;

import com.autelhome.eventstore.CommandWrapper;
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
public class CommandWrapperDeserializer {

    private final Schema commandWrapperSchema;
    private final MessageDeserializer messageDeserializer;

    public CommandWrapperDeserializer() {
        commandWrapperSchema = ReflectData.get().getSchema(CommandWrapper.class);
        messageDeserializer = new MessageDeserializer();
    }

    public CommandWrapper deserialize(final byte[] data) {

        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(commandWrapperSchema);
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

    private CommandWrapper convert(final GenericRecord genericRecord) {
        final String commandType = genericRecord.get("command_type").toString();
        final Optional<Class> commandClass = getClassForClassName(commandType);
        final Object command = commandClass.isPresent() ? messageDeserializer.deserialize(commandClass.get(), ((ByteBuffer) genericRecord.get("command")).array()) : null;
        return new CommandWrapper(commandType,
                command);
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
