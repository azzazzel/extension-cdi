package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.inject.Named;
import org.axonframework.eventhandling.DomainEventData;
import org.axonframework.eventsourcing.snapshotting.SnapshotFilter;

import java.util.function.Predicate;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.successes;

@Named
public class SimpleSnapshotFilter implements SnapshotFilter {

    @Override
    public boolean allow(DomainEventData<?> snapshotData) {
        successes.get().put("snapshotFilter", true);
        return false;
    }

    @Override
    public SnapshotFilter combine(SnapshotFilter other) {
        return null;
    }

    @Override
    public boolean test(DomainEventData<?> domainEventData) {
        return false;
    }

    @Override
    public Predicate<DomainEventData<?>> and(Predicate<? super DomainEventData<?>> other) {
        return null;
    }

    @Override
    public Predicate<DomainEventData<?>> negate() {
        return null;
    }

    @Override
    public Predicate<DomainEventData<?>> or(Predicate<? super DomainEventData<?>> other) {
        return null;
    }
}
