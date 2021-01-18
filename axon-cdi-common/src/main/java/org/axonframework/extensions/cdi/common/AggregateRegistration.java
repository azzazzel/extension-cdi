package org.axonframework.extensions.cdi.common;

import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AggregateRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateRegistration.class);


    @SuppressWarnings("unchecked")
    public static <A> void register(Set<AggregateInfo> aggregates, Configurer configurer) {

        Map<AggregateInfo, Map<Class<? extends A>, String>> hierarchy = buildAggregateHierarchy(aggregates);
        for (Map.Entry<AggregateInfo, Map<Class<? extends A>, String>> aggregateEntry : hierarchy.entrySet()) {
            AggregateInfo aggregateInfo = aggregateEntry.getKey();
            Map<Class<? extends A>, String> aggregateSubtypes = aggregateEntry.getValue();
            Class<A> aggregateType = (Class<A>) aggregateInfo.classType;
            String aggregateName = aggregateInfo.beanName;


            LOGGER.debug("Getting default aggregate configurer for " + aggregateType);
            AggregateConfigurer<A> aggregateConfigurer = AggregateConfigurer.defaultConfiguration(aggregateType);
//            aggregateConfigurer.withSubtypes(aggregateSubtypes.keySet());

            /*
                TODO:
                    move the repository creation logic to a place where CDI context is available
                    by the time we reach here proper repo should be in AggregateInfo
             */
            //


//            if (EMPTY_STRING.equals(aggregateInfo.getRepository())) {
//                String repositoryName = lcFirst(aggregateType.getSimpleName()) + "Repository";
//                String factoryName =  lcFirst(aggregateName) + "AggregateFactory";
//                if (beanFactory.containsBean(repositoryName)) {
//                    aggregateConfigurer.configureRepository(c -> beanFactory.getBean(repositoryName, Repository.class));
//                } else {
//                    registry.registerBeanDefinition(repositoryName,
//                            genericBeanDefinition(RepositoryFactoryBean.class)
//                                    .addConstructorArgValue(aggregateConfigurer)
//                                    .getBeanDefinition());
//
//                    if (!registry.isBeanNameInUse(factoryName)) {
//                        registry.registerBeanDefinition(factoryName,
//                                genericBeanDefinition(SpringPrototypeAggregateFactory.class)
//                                        .addConstructorArgValue(aggregateName)
//                                        .addConstructorArgValue(aggregateEntry.getValue())
//                                        .getBeanDefinition());
//                    }
//                    aggregateConfigurer.configureAggregateFactory(
//                            c -> beanFactory.getBean(factoryName, AggregateFactory.class)
//                    );
//
//                    String triggerDefinitionBeanName = aggregateAnnotation.snapshotTriggerDefinition();
//                    if (nonEmptyBeanName(triggerDefinitionBeanName)) {
//                        aggregateConfigurer.configureSnapshotTrigger(
//                                c -> beanFactory.getBean(triggerDefinitionBeanName, SnapshotTriggerDefinition.class)
//                        );
//                    }
//
//                    String cacheBeanName = aggregateAnnotation.cache();
//                    if (nonEmptyBeanName(cacheBeanName)) {
//                        aggregateConfigurer.configureCache(c -> beanFactory.getBean(cacheBeanName, Cache.class));
//                    }
//
//                    if (AnnotationUtils.isAnnotationPresent(aggregateType, "javax.persistence.Entity")) {
//                        aggregateConfigurer.configureRepository(
//                                c -> GenericJpaRepository.builder(aggregateType)
//                                        .parameterResolverFactory(c.parameterResolverFactory())
//                                        .handlerDefinition(c.handlerDefinition(aggregateType))
//                                        .lockFactory(c.getComponent(
//                                                LockFactory.class, () -> NullLockFactory.INSTANCE
//                                        ))
//                                        .entityManagerProvider(c.getComponent(
//                                                EntityManagerProvider.class,
//                                                () -> beanFactory.getBean(EntityManagerProvider.class)
//                                        ))
//                                        .eventBus(c.eventBus())
//                                        .repositoryProvider(c::repository)
//                                        .build()
//                        );
//                    }
//                }
//            } else {




//            aggregateConfigurer.configureRepository(
////                    configuration -> MyOwnRepo.builder(aggregateType)
////                                .eventStore(configuration.eventStore())
////                                .build()
////                     configuration -> {
////                         System.out.println("====== " + configuration.eventStore());
////                         return new MyOwnRepo<>(aggregateInfo, configuration);
////                    });
//            configuration -> {
//                System.out.println("====== " + configuration.eventStore());
//                return new MyOwnRepo2(aggregateType);
//            });


            Optional.ofNullable(aggregateInfo.aggregateFactory).ifPresent(aggregateFactory ->
                    aggregateConfigurer.configureAggregateFactory(
                            configuration -> {
                                LOGGER.debug("Configure aggregate's factory from " + aggregateInfo);
                                return aggregateFactory;
                            }
                    )
            );

            Optional.ofNullable(aggregateInfo.repository).ifPresent(
                    repository -> aggregateConfigurer.configureRepository(
                            configuration -> {
                                LOGGER.debug("Configure aggregate's repository from " + aggregateInfo);
                                return repository;
                            }
                    )
            );
//            }

//            String snapshotFilterBeanName = aggregateAnnotation.snapshotFilter();
//            if (nonEmptyBeanName(snapshotFilterBeanName)) {
//                aggregateConfigurer.configureSnapshotFilter(
//                        c -> getBean(snapshotFilterBeanName, c)
//                );
//            }
            Optional.ofNullable(aggregateInfo.snapshotFilter).ifPresent(
                    snapshotFilter -> aggregateConfigurer.configureSnapshotFilter(
                            configuration -> {
                                LOGGER.debug("Configure aggregate's snapshot filter from " + aggregateInfo);
                                return snapshotFilter;
                            }
                    )
            );


//            String commandTargetResolverBeanName = aggregateAnnotation.commandTargetResolver();
//            if (nonEmptyBeanName(commandTargetResolverBeanName)) {
//                aggregateConfigurer.configureCommandTargetResolver(
//                        c -> getBean(commandTargetResolverBeanName, c)
//                );
//            } else {
//                findComponent(CommandTargetResolver.class).ifPresent(
//                        commandTargetResolver -> aggregateConfigurer.configureCommandTargetResolver(
//                                c -> getBean(commandTargetResolver, c)
//                        )
//                );
//            }
            Optional.ofNullable(aggregateInfo.commandTargetResolver).ifPresent(
                    commandTargetResolver -> aggregateConfigurer.configureCommandTargetResolver(
                            configuration -> {
                                LOGGER.debug("Configure aggregate's command target resolver from " + aggregateInfo);
                                return commandTargetResolver;
                            }
                    )
            );

//            aggregateConfigurer.configureFilterEventsByType(c -> aggregateAnnotation.filterEventsByType());
            aggregateConfigurer.configureFilterEventsByType(
                    configuration -> {
                        LOGGER.debug("Configure aggregate's filter events by type from " + aggregateInfo);
                        return aggregateInfo.filterEventsByType;
                    }
            );


            LOGGER.debug("Register aggregate config: " + aggregateConfigurer);
            configurer.configureAggregate(aggregateConfigurer);

        }
    }

    private static <A> Map<AggregateInfo, Map<Class<? extends A>, String>> buildAggregateHierarchy(Set<AggregateInfo> aggregates) {
        Map<AggregateInfo, Map<Class<? extends A>, String>> hierarchy = new HashMap<>();
        for (AggregateInfo aggregateInfo : aggregates) {
            hierarchy.put(aggregateInfo, Collections.emptyMap());
//                Class<A> aggregateType = (Class<A>) beanFactory.getType(prototype);
//                SpringAggregate<A> springAggregate = new SpringAggregate<>(prototype, aggregateType);
//                Class<? super A> topType = topAnnotatedAggregateType(aggregateType);
//                SpringAggregate<? super A> topSpringAggregate = new SpringAggregate<>(beanName(topType), topType);
//                hierarchy.compute(topSpringAggregate, (type, subtypes) -> {
//                    if (subtypes == null) {
//                        subtypes = new HashMap<>();
//                    }
//                    if (!type.equals(springAggregate)) {
//                        subtypes.put(aggregateType, prototype);
//                    }
//                    return subtypes;
//                });
        }
        return hierarchy;
    }

    /**
     * Return the given {@code string}, with its first character lowercase
     *
     * @param string The input string
     * @return The input string, with first character lowercase
     */
    private static String lcFirst(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    private <A> Class<? super A> topAnnotatedAggregateType(Class<A> type) {
        Class<? super A> top = type;
        Class<? super A> topAnnotated = top;
//        while(!top.getSuperclass().equals(Object.class)) {
//            top = top.getSuperclass();
//            if (top.isAnnotationPresent(Aggregate.class)) {
//                topAnnotated = top;
//            }
//        }
        return topAnnotated;
    }

}
