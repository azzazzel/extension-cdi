package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.inject.Named;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.extensions.cdi.jakarta.annotations.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Named("MySimpleAggregate")
public class SimpleAggregate {

    @AggregateIdentifier
    UUID id;

//    @Inject
//    EventGateway eventGateway;

    public SimpleAggregate () {}

    @CommandHandler
    public SimpleAggregate(CreateSimpleAggregateCommand createSimpleAggregateCommand) {
        System.out.println("got createSimpleAggregateCommand");
        apply(new SimpleAggregateCreatedEvent());
        System.out.println("sent simpleAggregateCreatedEvent");
    }

    @EventSourcingHandler
    protected void handle(SimpleAggregateCreatedEvent simpleAggregateCreatedEvent) {
        System.out.println("received simpleAggregateCreatedEvent");
    }
}
