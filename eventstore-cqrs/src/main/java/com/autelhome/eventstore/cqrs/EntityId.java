package com.autelhome.eventstore.cqrs;

import java.util.Objects;
import java.util.UUID;

/**
 * @author xdeclercq
 */
public class EntityId {

    private final String id;

    public EntityId() {
        id = UUID.randomUUID().toString();
    }

    private EntityId(final String id) {
        this.id = id;
    }

    public static EntityId fromId(final String id) {
        return new EntityId(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EntityId entityId = (EntityId) o;
        return Objects.equals(id, entityId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
