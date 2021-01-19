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
import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.successes;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(
        cache = "simpleCache",
//        snapshotTriggerDefinition = "noSnapshotTriggerDefinition",
        snapshotTriggerDefinition = "simpleSnapshotTriggerDefinition",
        snapshotFilter = "simpleSnapshotFilter"
)
@Named("MySimpleConfiguredAggregate")
public class SimpleConfiguredAggregate {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleConfiguredAggregate.class);

    @AggregateIdentifier
    UUID id;


    public SimpleConfiguredAggregate() {}

    @CommandHandler
    public SimpleConfiguredAggregate(CreateSimpleAggregateCommand createSimpleAggregateCommand) {
        LOGGER.debug("got createSimpleAggregateCommand");
        apply(new SimpleAggregateCreatedEvent(createSimpleAggregateCommand.id));
        LOGGER.debug("sent simpleAggregateCreatedEvent");
    }

    @EventSourcingHandler
    protected void handle(SimpleAggregateCreatedEvent simpleAggregateCreatedEvent) {
        id = simpleAggregateCreatedEvent.id;
        LOGGER.debug("received simpleAggregateCreatedEvent");
        successes.get().put("aggregate", true);
    }
}
