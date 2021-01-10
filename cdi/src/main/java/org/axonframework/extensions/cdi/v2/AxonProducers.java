package org.axonframework.extensions.cdi.v2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Typed;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.AxonConfigurationException;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.cdi.v2.optionals.MissingEventScheduler;
import org.axonframework.extensions.cdi.v2.optionals.MissingEventStore;
import org.axonframework.extensions.cdi.v2.qualifier.Internal;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@ApplicationScoped
@Internal
public class AxonProducers implements Serializable {

    private static Logger LOGGER = LoggerFactory.getLogger(AxonProducers.class);

    static {
        LOGGER.debug("Axon Producers is initialized");
    }

    @Produces
    @ApplicationScoped
    @Typed(Configurer.class)
    @Internal
    public Configurer configurer() {
        LOGGER.debug("producing Configurer");
        return DefaultConfigurer.defaultConfiguration();
    }

    @Produces
    @ApplicationScoped
    @Typed(CommandBus.class)
    @Default
    public CommandBus commandBus(Configuration configuration) {
        LOGGER.debug("producing CommandBus");
        return configuration.commandBus();
    }

    @Produces
    @ApplicationScoped
    @Typed(CommandGateway.class)
    @Default
    public CommandGateway commandGateway(Configuration configuration) {
        LOGGER.debug("producing CommandGateway");
        return configuration.commandGateway();
    }

    @Produces
    @Typed(QueryBus.class)
    @ApplicationScoped
    @Default
    public QueryBus queryBus(Configuration configuration) {
        LOGGER.debug("producing QueryBus");
        return configuration.queryBus();
    }

    @Produces
    @ApplicationScoped
    @Typed(QueryGateway.class)
    @Default
    public QueryGateway queryGateway(Configuration configuration) {
        LOGGER.debug("producing QueryGateway");
        return configuration.queryGateway();
    }

    @Produces
    @ApplicationScoped
    @Typed(QueryUpdateEmitter.class)
    @Default
    public QueryUpdateEmitter queryUpdateEmitter(Configuration configuration) {
        LOGGER.debug("producing QueryUpdateEmitter");
        return configuration.queryUpdateEmitter();
    }

    @Produces
    @ApplicationScoped
    @Typed(EventBus.class)
    @Default
    public EventBus eventBus(Configuration configuration) {
        LOGGER.debug("producing EventBus");
        return configuration.eventBus();
    }

    @Produces
    @ApplicationScoped
    @Typed(EventGateway.class)
    @Default
    public EventGateway eventGateway(Configuration configuration) {
        LOGGER.debug("producing EventGateway");
        return configuration.eventGateway();
    }

    @Produces
    @ApplicationScoped
    @Typed(EventStore.class)
    @Default
    public EventStore eventStore(Configuration configuration) {
        LOGGER.debug("producing EventStore");
        try {
            return configuration.eventStore();
        } catch (AxonConfigurationException e) {
            return new MissingEventStore(e.getMessage());
        }
    }

    @Produces
    @ApplicationScoped
    @Typed(EventScheduler.class)
    @Default
    public EventScheduler eventScheduler(Configuration configuration) {
        LOGGER.debug("producing EventScheduler");
        EventScheduler eventScheduler = configuration.eventScheduler();
        if (eventScheduler == null) {
            eventScheduler = new MissingEventScheduler("No EventScheduler is configured");
        }
        return eventScheduler;
    }

    @Produces
    @ApplicationScoped
    @Typed(Serializer.class)
    @Default
    public Serializer eventSerializer(Configuration configuration) {
        LOGGER.debug("producing Serializer");
        return configuration.serializer();
    }

    @Produces
    @ApplicationScoped
    @Typed(DeadlineManager.class)
    @Default
    public DeadlineManager eventDeadlineManager(Configuration configuration) {
        LOGGER.debug("producing DeadlineManager");
        return configuration.deadlineManager();
    }

}
