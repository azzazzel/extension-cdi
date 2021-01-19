package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.inject.Named;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.extensions.cdi.jakarta.annotations.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.success;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Named("MySimpleAggregate")
public class SimpleAggregate {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleAggregate.class);

    @AggregateIdentifier
    UUID id;


    public SimpleAggregate () {}

    @CommandHandler
    public SimpleAggregate(CreateSimpleAggregateCommand createSimpleAggregateCommand) {
        LOGGER.debug("got createSimpleAggregateCommand");
        apply(new SimpleAggregateCreatedEvent(createSimpleAggregateCommand.id));
        LOGGER.debug("sent simpleAggregateCreatedEvent");
    }

    @EventSourcingHandler
    protected void handle(SimpleAggregateCreatedEvent simpleAggregateCreatedEvent) {
        id = simpleAggregateCreatedEvent.id;
        LOGGER.debug("received simpleAggregateCreatedEvent");
        success.set(true);
    }
}
