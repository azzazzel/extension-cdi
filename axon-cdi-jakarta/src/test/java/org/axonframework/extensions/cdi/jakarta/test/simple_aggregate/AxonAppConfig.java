package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.axonframework.eventsourcing.NoSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.extensions.cdi.jakarta.annotations.AxonConfig;

public class AxonAppConfig {

    @Produces
    @AxonConfig
    public EventStorageEngine eventStore() {
        return new InMemoryEventStorageEngine();
    }

    @Produces
    @AxonConfig
    @Named
    public SnapshotTriggerDefinition noSnapshotTriggerDefinition() {
        return NoSnapshotTriggerDefinition.INSTANCE;
    }

}
