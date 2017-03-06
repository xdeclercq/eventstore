package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface CommandHandlerRegistry {

    <C extends Object> RegistryEntry<C> register(final CommandHandler<C> handler);
}
