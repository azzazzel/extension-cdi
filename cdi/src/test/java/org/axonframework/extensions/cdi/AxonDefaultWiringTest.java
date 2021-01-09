package org.axonframework.extensions.cdi;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Inject;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ArquillianExtension.class)
public class AxonDefaultWiringTest {

    static File[] files = Maven.resolver().loadPomFromFile("pom.xml")
            .importRuntimeDependencies().resolve().withTransitivity().asFile();

    @Inject private Instance<Configuration> axonConfiguration;

    @Inject private Instance<QueryGateway> queryGateway;
    @Inject private Instance<QueryBus> queryBus;
    @Inject private Instance<QueryUpdateEmitter> queryUpdateEmitter;

    @Inject private Instance<CommandBus> commandBus;
    @Inject private Instance<CommandGateway> commandGateway;

    @Inject private Instance<EventBus> eventBus;
    @Inject private Instance<EventGateway> eventGateway;

    @Inject private Instance<Serializer> serializer;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsServiceProvider(Extension.class, AxonCdiExtension.class)
//                .addPackage("org.axonframework.extensions.cdi")
//                .addClass(AxonDefaultConfiguration.class.getName())
//                .addAsLibraries(files)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @TestFactory
    public Stream<DynamicTest> defaultObjectIsWired() {
        return Arrays.stream(this.getClass().getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(Instance.class) )
                .map(f -> DynamicTest.dynamicTest(
                        "Testing if " + f.getName() + " is wired",
                        () -> {
                            Instance<?> instance = (Instance<?>) f.get(this);
                            assertTrue(instance.isResolvable(), f.getName() + " is not wired!");
                            assertEquals(1, instance.stream().count(), "More than one instance of " + f.getName() + " wired!");
                        })
                );
    }


}
