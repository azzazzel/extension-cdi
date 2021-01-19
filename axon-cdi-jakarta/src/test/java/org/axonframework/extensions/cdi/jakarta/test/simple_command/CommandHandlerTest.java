package org.axonframework.extensions.cdi.jakarta.test.simple_command;

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

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ArquillianExtension.class)
public class CommandHandlerTest {

    private static Logger LOGGER = LoggerFactory.getLogger(CommandHandlerTest.class);

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        archive
                .addClass(SimpleCommandHandler.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));
        return archive;
    }

    @Inject
    CommandGateway commandGateway;

    private String message = "test command";

    @Test
    public void test() {
        try {
            String result = (String) commandGateway.send(new SimpleCommand(message)).get();
            assertEquals(echo(message), result);
            result = (String) commandGateway.send(new AnotherSimpleCommand(message)).get();
            assertEquals(echo(message), result);
        } catch (Exception e) {
            fail("Sending command FAILED with " + e);
        }
    }
}
