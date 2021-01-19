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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.success;
import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.successes;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ArquillianExtension.class)
public class SimpleConfiguredAggregateTest {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleConfiguredAggregateTest.class);

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        archive
                .addPackage(SimpleConfiguredAggregateTest.class.getPackage().getName())
                .deleteClass(SimpleAggregate.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));
        return archive;
    }

    @Inject
    CommandGateway commandGateway;

    @Test
    public void test() {
        Map<String, Boolean> componentResults = new HashMap<>();
        componentResults.put("cache", false);
        componentResults.put("aggregate", false);
        componentResults.put("snapshotTriggerDefinition", false);

        componentResults.put("snapshotFilter", false);
        componentResults.put("commandTargetResolver", false);

        successes.set(componentResults);

        try {
            commandGateway.send(new CreateSimpleAggregateCommand(UUID.randomUUID()));
            LOGGER.info("Command sent");
            System.out.println(successes.get());
            assertTrue(successes.get().get("cache"));
            assertTrue(successes.get().get("snapshotTriggerDefinition"));
            assertTrue(successes.get().get("aggregate"));

            /*
            TODO: figure out how to test those:
             */

//            assertTrue(successes.get().get("snapshotFilter"));
//            assertTrue(successes.get().get("commandTargetResolver"));


        } catch (Exception e) {
            LOGGER.info("Sending command FAILED", e);
            fail("Sending command FAILED", e);
        }
    }

}
