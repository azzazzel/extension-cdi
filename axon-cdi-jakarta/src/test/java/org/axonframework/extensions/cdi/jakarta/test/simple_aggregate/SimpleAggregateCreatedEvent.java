package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.UUID;

public class SimpleAggregateCreatedEvent {

    @AggregateIdentifier
    public UUID id;

    public SimpleAggregateCreatedEvent(UUID id) {
        this.id = id;
    }
}
