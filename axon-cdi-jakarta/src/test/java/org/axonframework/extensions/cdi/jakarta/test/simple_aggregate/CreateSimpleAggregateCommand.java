package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class CreateSimpleAggregateCommand {

    public CreateSimpleAggregateCommand(UUID id) {
        this.id = id;
    }

    @TargetAggregateIdentifier
    public UUID id = null;
}
