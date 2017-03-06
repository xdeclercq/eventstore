package com.autelhome.eventstore.cqrs;

/**
 * @author xdeclercq
 */
public interface Repository<T extends Aggregate> {

    void save(final T aggregate, final int expectedVersion);

    T getById(final EntityId id);
}
