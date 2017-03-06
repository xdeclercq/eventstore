package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface EventHandlerRegistry {

    <E extends Object> RegistryEntry<E> register(final EventHandler<E> handler);
}
