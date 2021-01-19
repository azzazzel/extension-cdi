package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.modelling.command.CommandTargetResolver;
import org.axonframework.modelling.command.VersionedAggregateIdentifier;

public class SimpleCommandTargetResolver implements CommandTargetResolver {

    @Override
    public VersionedAggregateIdentifier resolveTarget(CommandMessage<?> command) {
        return null;
    }
}
