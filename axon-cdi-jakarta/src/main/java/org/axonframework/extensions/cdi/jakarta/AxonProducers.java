package org.axonframework.extensions.cdi.jakarta;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Typed;
import jakarta.enterprise.inject.spi.Bean;
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
import org.axonframework.extensions.cdi.common.AggregateInfo;
import org.axonframework.extensions.cdi.jakarta.annotations.Aggregate;
import org.axonframework.extensions.cdi.jakarta.annotations.AxonConfig;
import org.axonframework.extensions.cdi.jakarta.annotations.ExternalCommandHandler;
import org.axonframework.extensions.cdi.jakarta.annotations.Internal;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@ApplicationScoped
@Internal
public class AxonProducers extends AbstractAxonProducers implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AxonProducers.class);

    static {
        LOGGER.info("Axon Producers (jakarta version) is loaded");
    }

    @Inject
    BeanManager beanManager;

    private AnnotationLiteral<Any> anyBean = new AnnotationLiteral<Any>() {
    };

    protected void discoverAxonObjects() {
        LOGGER.debug("scanning for known beans");
        beanManager.getBeans(Object.class, anyBean).stream()
                .forEach(bean -> {

                    if (bean.getStereotypes().contains(AxonConfig.class)) {
                        LOGGER.debug("found axon configuration bean: " + bean);
                        Object object = instantiate(bean, Object.class);
                        if (EventStorageEngine.class.isAssignableFrom(object.getClass())) {
                            eventStorageEnginesSet.add((EventStorageEngine) object);
                        }
                    }

                    if (bean.getStereotypes().contains(Aggregate.class)) {
                        LOGGER.debug("found aggregate: " + bean);

                        Aggregate aggregate = bean.getBeanClass().getAnnotation(Aggregate.class);
                        System.out.println("\t cache: " + aggregate.cache());
                        System.out.println("\t commandTargetResolver: " + aggregate.commandTargetResolver());
                        System.out.println("\t filterEventsByType: " + aggregate.filterEventsByType());
                        System.out.println("\t repo: " + aggregate.repository());
                        System.out.println("\t snapshotFilter: " + aggregate.snapshotFilter());
                        System.out.println("\t snapshotTriggerDefinition: " + aggregate.snapshotTriggerDefinition());
                        System.out.println("\t type: " + aggregate.type());

                        AggregateInfo aggregateInfo = new AggregateInfo(bean.getName(), bean.getBeanClass());
                        aggregateInfoSet.add(aggregateInfo);
                    }

                    if (bean.getStereotypes().contains(ExternalCommandHandler.class)) {
                        LOGGER.debug("found command handler: " + bean);
                        commandHandlersSet.add(instantiate(bean, Object.class));
                    }

                });
    }

    private <T> T instantiate(Bean bean, Class<T> type) {
        CreationalContext creationalContext = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, type, creationalContext);
    }


//    @Override
//    protected Set<AggregateInfo> getAggregates() {
//        scanContext();
//        return aggregateInfoSet;
//
////        LOGGER.debug("collecting annotated aggregates");
////        return beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {}).stream()
////                .filter(bean -> bean.getStereotypes().contains(Aggregate.class))
////                .map(bean -> {
////                    AggregateInfo aggregateInfo = new AggregateInfo (bean.getName(), bean.getBeanClass());
////                    return aggregateInfo;
////                })
////                .collect(Collectors.toSet());
////                .forEach(bean -> {
////                    System.out.println("BEAN: " + bean);
////                    System.out.println("\tBEAN scope: " + bean.getScope());
////                    bean.getStereotypes().forEach(aClass ->
////                            System.out.println("\tstereotype: " + aClass)
////                    );
////                });
//
//
////        return beanManager.getBeans(Object.class, new AnnotationLiteral<Aggregate>() {}).stream()
////                .peek(bean -> LOGGER.debug("found aggregate: " + bean))
////                .map(bean -> {
////                    bean.getStereotypes().forEach(annotation ->
////                            System.out.println("ANNOTATION: " + annotation)
////                    );
////
//////                    AggregateModel aggregateModel = AnnotatedAggregateMetaModelFactory
////////                            .inspectAggregate(bean.getBeanClass(), subtypes.keySet());
//////                            .inspectAggregate(bean.getBeanClass());
////////                    CDIAggregateFactory aggregateFactory = new CDIAggregateFactory(aggregateModel);
//////                    CDIAggregateFactory2 aggregateFactory = new CDIAggregateFactory2(bean, Collections.emptyMap());
////                    AggregateInfo aggregateInfo = new AggregateInfo (bean.getName(), bean.getBeanClass());
//////                    aggregateInfo.setAggregateFactory(aggregateFactory);
////                    return aggregateInfo;
////                })
////                .collect(Collectors.toSet());
//    }


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
