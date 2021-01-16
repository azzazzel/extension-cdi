package org.axonframework.extensions.cdi.javax.test;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.cdi.javax.AxonProducers;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(ArquillianExtension.class)
public class AxonDefaultWiringTest extends ArchiveTemplates {

    private static Logger LOGGER = LoggerFactory.getLogger(AxonDefaultWiringTest.class);

    @Internal @Inject private Instance<AxonProducers> producers;
    @Internal @Inject private Instance<Configurer> axonConfigurer;

    @Inject private Instance<Configuration> axonConfiguration;

    @Inject private Instance<QueryGateway> queryGateway;
    @Inject private Instance<QueryBus> queryBus;
    @Inject private Instance<QueryUpdateEmitter> queryUpdateEmitter;

    @Inject private Instance<CommandBus> commandBus;
    @Inject private Instance<CommandGateway> commandGateway;

    @Inject private Instance<EventBus> eventBus;
    @Inject private Instance<EventGateway> eventGateway;
    @Inject private Instance<EventStore> eventStore;
    @Inject private Instance<EventScheduler> eventScheduler;

    @Inject private Instance<Serializer> serializer;

    @Inject private Instance<DeadlineManager> deadlineManager;

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));
        return archive;
    }

    @Test
    public void test () {
//        jakarta.enterprise.inject.Instance<?> instance =
//                jakarta.enterprise.inject.spi.CDI.current()
//                        .select(CommandGateway.class,
//                                new jakarta.enterprise.util.AnnotationLiteral<jakarta.enterprise.inject.Default>() {}
//                                );
//        System.out.println(instance);
//        System.out.println(instance.isResolvable());


//        System.out.println(
//            CDI.current()
//                    .select(CommandGateway.class,
//                            new AnnotationLiteral<Default>() {}
//                            )
//        );
    }


    @TestFactory
    @DisplayName("Axon OOTB components are wired?")
    public Stream<DynamicTest> axonComponentsAreWired() {
        return Arrays.stream(this.getClass().getDeclaredFields())
                .filter(isCdiInstance)
                .filter(isTestable)
                .filter(isInternal.negate())
                .map(testFieldIsProperlyInjected);
    }
    @TestFactory
    @DisplayName("Axon internal components are not wired?")
    public Stream<DynamicTest> axonInternalComponentsAreNotWired() {
        return Arrays.stream(this.getClass().getDeclaredFields())
                .filter(isCdiInstance)
                .filter(isTestable)
                .filter(isInternal)
                .map(testFieldIsNotInjected);
    }

    private final Function<Field, DynamicTest> testFieldIsProperlyInjected = field -> {
            String fieldName = field.getName();
            return DynamicTest.dynamicTest(
                    fieldName + " is wired",
                    () -> {
                        Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        LOGGER.debug("Testing wiring for field: " + type.getTypeName());

                        Instance<?> instance = (Instance<?>) field.get(this);
                        assertFalse(instance.isUnsatisfied(), "Instance of " + fieldName + " is unsatisfied?");
                        assertFalse(instance.isAmbiguous(), "Instance of " + fieldName + " is ambiguous?");
                        assertTrue(instance.isResolvable(), "Instance of " + fieldName + " is resolvable?");
                        assertEquals(1, instance.stream().count(), "Number of instances of " + fieldName + " ?");
                        assertTrue(successfullyExecuteMethodOnInstance(instance), "An object for " + fieldName + " is wired?");

                        LOGGER.debug("Done testing wiring for field: " + type);
                    });
    };

    private final Function<Field, DynamicTest> testFieldIsNotInjected = field -> {
        String fieldName = field.getName();
        return DynamicTest.dynamicTest(
                fieldName + " is NOT wired",
                () -> {
                    Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    LOGGER.debug("Testing wiring for field: " + type.getTypeName());

                    Instance<?> instance = (Instance<?>) field.get(this);
                    assertFalse(instance.isResolvable(), "Instance of " + fieldName + " is resolvable?");
                    LOGGER.debug("Done testing wiring for field: " + type);
                });
    };


    private boolean successfullyExecuteMethodOnInstance(Instance instance) {
        try {
            instance.get().toString();
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to execute method on instance " + instance, e);
            return false;
        }
    }

    private final Predicate<Field> isCdiInstance = field -> field.getType().isAssignableFrom(Instance.class);
    private final Predicate<Field> isTestable = field -> !field.isAnnotationPresent(SkipTest.class);
    private final Predicate<Field> isInternal = field -> field.isAnnotationPresent(Internal.class);

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SkipTest {}

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Internal {}

}