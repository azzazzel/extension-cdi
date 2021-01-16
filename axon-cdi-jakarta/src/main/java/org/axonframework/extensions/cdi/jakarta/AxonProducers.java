package org.axonframework.extensions.cdi.jakarta;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Typed;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
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
import org.axonframework.extensions.cdi.jakarta.annotations.Aggregate;
import org.axonframework.extensions.cdi.jakarta.annotations.ExternalCommandHandler;
import org.axonframework.extensions.cdi.jakarta.annotations.Internal;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
@Internal
public class AxonProducers extends AbstractAxonProducers implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AxonProducers.class);

    static {
        LOGGER.info("Axon Producers (jakarta version) is loaded");
    }

    @Inject
    @ExternalCommandHandler
    protected Instance<Object> commandHandlers;

    @Inject
    BeanManager beanManager;

    @Inject
    Instance<EventStorageEngine> eventStorageEngines;

    @Override
    protected Stream<Object> getCommandHandlers() {
        LOGGER.debug("collecting annotated command handlers");
        return commandHandlers.stream()
                .peek(o -> LOGGER.debug("found command handler: " + o));
    }

    @Override
    protected Stream<Class> getAggregates() {
        LOGGER.debug("collecting annotated aggregates");

        return beanManager.getBeans(Object.class, new AnnotationLiteral<Aggregate>() {}).stream()
                .peek(bean -> LOGGER.debug("found aggregate: " + bean))
                .map(bean -> (Class) bean.getBeanClass());
    }

    @Override
    protected Optional<EventStorageEngine> getEventStore() {
        LOGGER.debug("looking for event storage engine");
        return eventStorageEngines.stream()
                .peek(bean -> LOGGER.debug("found event storage engine: " + bean))
                .findFirst();
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
