package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.inject.Inject;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.extensions.cdi.jakarta.test.ArchiveTemplates;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.success;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ArquillianExtension.class)
public class SimpleAggregateTest {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleAggregateTest.class);

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        archive
                .addPackage(SimpleAggregateTest.class.getPackage().getName())
                .deleteClass(SimpleConfiguredAggregate.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));
        return archive;
    }

    @Inject
    CommandGateway commandGateway;

    @Test
    public void test() {
        success.set(false);
        try {
            commandGateway.send(new CreateSimpleAggregateCommand(UUID.randomUUID()));
            LOGGER.info("Command sent");
            assertTrue(success.get());
        } catch (Exception e) {
            LOGGER.info("Sending command FAILED", e);
            fail("Sending command FAILED", e);
        }
    }

}
