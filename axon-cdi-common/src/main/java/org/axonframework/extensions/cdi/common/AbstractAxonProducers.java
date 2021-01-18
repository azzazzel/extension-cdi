package org.axonframework.extensions.cdi.common;

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
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.axonframework.extensions.cdi.common.AggregateRegistration.register;

public abstract class AbstractAxonProducers {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAxonProducers.class);

    protected Set<AggregateInfo> aggregateInfoSet = new HashSet<>();
    protected Set<Object> commandHandlersSet = new HashSet<>();
    protected Set<EventStorageEngine> eventStorageEnginesSet = new HashSet<>();
    private boolean discoveryComplete = false;

    protected abstract void discoverAxonObjects();

    public Configuration configuration() {
        LOGGER.debug("Building Axon Framework configuration");

        if (!discoveryComplete) {
            discoverAxonObjects();
            discoveryComplete = true;
        }

        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        if (eventStorageEnginesSet.size() > 1) {
            LOGGER.warn("More than one EventStorageEngine registered: " + eventStorageEnginesSet);
        }

        eventStorageEnginesSet.stream().findFirst().ifPresent(eventStore -> {
            LOGGER.debug("Registering event store: " + eventStore);
            configurer.configureEmbeddedEventStore(configuration -> eventStore);
        });

        commandHandlersSet.stream()
                .forEach(commandHandler -> {
                    LOGGER.debug("Registering command handler: " + commandHandler);
                    configurer.registerCommandHandler(configuration -> commandHandler);
                });

        register(aggregateInfoSet, configurer);

        Configuration configuration = configurer.buildConfiguration();
        configuration.start();

        return configuration;
    }


    public CommandBus commandBus(Configuration configuration) {
        LOGGER.debug("producing CommandBus");
        return configuration.commandBus();
    }

    public CommandGateway commandGateway(Configuration configuration) {
        LOGGER.debug("producing CommandGateway");
        return configuration.commandGateway();
    }

    public QueryBus queryBus(Configuration configuration) {
        LOGGER.debug("producing QueryBus");
        return configuration.queryBus();
    }

    public QueryGateway queryGateway(Configuration configuration) {
        LOGGER.debug("producing QueryGateway");
        return configuration.queryGateway();
    }

    public QueryUpdateEmitter queryUpdateEmitter(Configuration configuration) {
        LOGGER.debug("producing QueryUpdateEmitter");
        return configuration.queryUpdateEmitter();
    }

    public EventBus eventBus(Configuration configuration) {
        LOGGER.debug("producing EventBus");
        return configuration.eventBus();
    }

    public EventGateway eventGateway(Configuration configuration) {
        LOGGER.debug("producing EventGateway");
        return configuration.eventGateway();
    }

    public EventStore eventStore(Configuration configuration) {
        LOGGER.debug("producing EventStore");
        try {
            return configuration.eventStore();
        } catch (AxonConfigurationException e) {
            return new MissingEventStore(e.getMessage());
        }
    }

    public EventScheduler eventScheduler(Configuration configuration) {
        LOGGER.debug("producing EventScheduler");
        EventScheduler eventScheduler = configuration.eventScheduler();
        if (eventScheduler == null) {
            eventScheduler = new MissingEventScheduler("No EventScheduler is configured");
        }
        return eventScheduler;
    }

    public Serializer eventSerializer(Configuration configuration) {
        LOGGER.debug("producing Serializer");
        return configuration.serializer();
    }

    public DeadlineManager eventDeadlineManager(Configuration configuration) {
        LOGGER.debug("producing DeadlineManager");
        return configuration.deadlineManager();
    }
}
