package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface CommandHandler<C> {

    /**
     * @param command an command
     */
    void handle(final C command);
}
