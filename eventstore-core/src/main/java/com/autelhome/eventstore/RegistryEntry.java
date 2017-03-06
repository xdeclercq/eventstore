package com.autelhome.eventstore;

/**
 * @author xdeclercq
 */
public interface RegistryEntry<T> {

    void forClass(final Class<T> type);
}
