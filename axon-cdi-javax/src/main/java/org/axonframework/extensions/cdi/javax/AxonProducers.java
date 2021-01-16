package org.axonframework.extensions.cdi.javax;


import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.cdi.common.AbstractAxonProducers;
import org.axonframework.extensions.cdi.javax.annotations.ExternalCommandHandler;
import org.axonframework.extensions.cdi.javax.annotations.Internal;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
@Internal
public class AxonProducers extends AbstractAxonProducers implements Serializable {

    private static Logger LOGGER = LoggerFactory.getLogger(AxonProducers.class);

    static {
        LOGGER.info("Axon Producers (javax version) is loaded");
    }

    @Inject
    @ExternalCommandHandler
    Instance<Object> externalCommandHandlers;

    @Override
    protected Stream<Object> getCommandHandlers() {
        return externalCommandHandlers.stream();
    }


    @Override
    protected Stream<Class> getAggregates() {
        return null;
    }

    @Override
    protected Optional<EventStorageEngine> getEventStore() {
        return Optional.empty();
    }

    @Default
    @Typed(Configuration.class)
    @ApplicationScoped
    @Produces
    @Override
    public Configuration configuration() {
        return super.configuration();
    }


    @Default
    @Typed(CommandBus.class)
    @ApplicationScoped
    @Produces
    @Override
    public CommandBus commandBus(Configuration configuration) {
        return super.commandBus(configuration);
    }

    @Default
    @Typed(CommandGateway.class)
    @ApplicationScoped
    @Produces
    @Override
    public CommandGateway commandGateway(Configuration configuration) {
        return super.commandGateway(configuration);
    }

    @Default
    @Typed(QueryBus.class)
    @ApplicationScoped
    @Produces
    @Override
    public QueryBus queryBus(Configuration configuration) {
        return super.queryBus(configuration);
    }

    @Default
    @Typed(QueryGateway.class)
    @ApplicationScoped
    @Produces
    @Override
    public QueryGateway queryGateway(Configuration configuration) {
        return super.queryGateway(configuration);
    }

    @Default
    @Typed(QueryUpdateEmitter.class)
    @ApplicationScoped
    @Produces
    @Override
    public QueryUpdateEmitter queryUpdateEmitter(Configuration configuration) {
        return super.queryUpdateEmitter(configuration);
    }

    @Default
    @Typed(EventBus.class)
    @ApplicationScoped
    @Produces
    @Override
    public EventBus eventBus(Configuration configuration) {
        return super.eventBus(configuration);
    }

    @Default
    @Typed(EventGateway.class)
    @ApplicationScoped
    @Produces
    @Override
    public EventGateway eventGateway(Configuration configuration) {
        return super.eventGateway(configuration);
    }

    @Default
    @Typed(EventStore.class)
    @ApplicationScoped
    @Produces
    @Override
    public EventStore eventStore(Configuration configuration) {
        return super.eventStore(configuration);
    }

    @Default
    @Typed(EventScheduler.class)
    @ApplicationScoped
    @Produces
    @Override
    public EventScheduler eventScheduler(Configuration configuration) {
        return super.eventScheduler(configuration);
    }

    @Default
    @Typed(Serializer.class)
    @ApplicationScoped
    @Produces
    @Override
    public Serializer eventSerializer(Configuration configuration) {
        return super.eventSerializer(configuration);
    }

    @Default
    @Typed(DeadlineManager.class)
    @ApplicationScoped
    @Produces
    @Override
    public DeadlineManager eventDeadlineManager(Configuration configuration) {
        return super.eventDeadlineManager(configuration);
    }
}
