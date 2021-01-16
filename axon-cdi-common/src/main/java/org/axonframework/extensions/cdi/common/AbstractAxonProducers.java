package org.axonframework.extensions.cdi.common;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.AxonConfigurationException;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.ScopeDescriptor;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public abstract class AbstractAxonProducers {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAxonProducers.class);

    protected abstract Stream<Object> getCommandHandlers();

    protected abstract Stream<Class> getAggregates();

    protected abstract Optional<EventStorageEngine> getEventStore();

    public <T> Configuration configuration() {
        LOGGER.debug("Building Axon Framework configuration");
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        getEventStore().ifPresent(eventStore ->
                configurer.configureEmbeddedEventStore(configuration -> eventStore));

        getCommandHandlers()
                .forEach(ech -> {
                    LOGGER.debug("Registering command handler: " + ech);
                    configurer.registerCommandHandler(configuration -> ech);
                });

        getAggregates()
                .forEach(aggregateClass -> {
                    LOGGER.debug("Registering aggregate: " + aggregateClass);
                    AggregateConfigurer aggregateConfigurer = AggregateConfigurer.defaultConfiguration (aggregateClass);
                    aggregateConfigurer.configureRepository(configuration -> {
                        return new Repository() {
                            @Override
                            public void send(Message<?> message, ScopeDescriptor scopeDescriptor) throws Exception {
                                System.out.println("REPO:: send " + message);
                            }

                            @Override
                            public boolean canResolve(ScopeDescriptor scopeDescriptor) {
                                System.out.println("REPO:: canResolve " + scopeDescriptor.scopeDescription());
                                return false;
                            }

                            @Override
                            public Aggregate load(String s) {
                                System.out.println("REPO:: load " + s);
                                return null;
                            }

                            @Override
                            public Aggregate load(String s, Long aLong) {
                                System.out.println("REPO:: canResolve " + aLong);
                                return null;
                            }

                            @Override
                            public Aggregate newInstance(Callable callable) throws Exception {
                                System.out.println("REPO:: canResolve " + callable);
                                return null;
                            }
                        };
                    });
                    aggregateConfigurer.configureAggregateFactory(configuration -> {
                        System.out.println("\n\n --- configureAggregateFactory !!!");
                       return new AggregateFactory() {
                           @Override
                           public Class getAggregateType() {
                               System.out.println("\n\n --- getAggregateType called !!!");
                               return aggregateClass;
                           }

                           @Override
                           public Object createAggregateRoot(String s, DomainEventMessage domainEventMessage) {
                               System.out.println("\n\n --- createAggregateRoot called !!!");
                               return null;
                           }
                       };
                    });
                    configurer.configureAggregate(aggregateConfigurer);
                });

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
