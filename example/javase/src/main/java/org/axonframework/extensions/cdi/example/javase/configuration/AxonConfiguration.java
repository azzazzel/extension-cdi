package org.axonframework.extensions.cdi.example.javase.configuration;

import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

@ApplicationScoped
public class AxonConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * In a Java SE environment, we are using an in-memory store.
     *
     * @return Event storage engine.
     */
    @Produces
    @ApplicationScoped
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }
}