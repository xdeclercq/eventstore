package com.autelhome.eventstore;

import org.apache.avro.reflect.AvroName;
import org.apache.avro.reflect.AvroSchema;

/**
 * @author xdeclercq
 */
@AvroSchema("{\n" +
        "  \"type\" : \"record\",\n" +
        "  \"name\" : \"CommandWrapper\",\n" +
        "  \"namespace\" : \"com.autelhome.eventstore\",\n" +
        "  \"fields\" : [ {\n" +
        "    \"name\" : \"command_type\",\n" +
        "    \"type\" : \"string\"\n" +
        "  }, {\n" +
        "    \"name\" : \"command\",\n" +
        "    \"type\" : \"bytes\"\n" +
        "  } ]\n" +
        "}")
public class CommandWrapper {

    @AvroName("command_type")
    private final String commandType;
    private final Object command;

    public CommandWrapper(final String commandType, final Object command) {
        this.commandType = commandType;
        this.command = command;
    }

    public String getCommandType() {
        return commandType;
    }

    public Object getCommand() {
        return command;
    }


    @Override
    public String toString() {
        return "EventWrapper{" +
                "commandType='" + commandType + '\'' +
                ", command=" + command +
                '}';
    }
}
