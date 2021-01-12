package org.axonframework.extensions.cdi.javax.test;

import org.axonframework.commandhandling.gateway.CommandGateway;

import org.axonframework.extensions.cdi.javax.test.component.SimpleCommandHandler;
import org.axonframework.extensions.cdi.javax.test.component.SimpleCommandHandler2;
import org.axonframework.extensions.cdi.javax.test.component.AnotherSimpleCommand;
import org.axonframework.extensions.cdi.javax.test.component.SimpleCommand;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.fail;

@Disabled
@ExtendWith(ArquillianExtension.class)
public class CommandHandlerTest {

    private static Logger LOGGER = LoggerFactory.getLogger(CommandHandlerTest.class);

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        archive
                .addClass(SimpleCommandHandler.class)
                .addClass(SimpleCommandHandler2.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");


        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));

        return archive;
    }

    @Inject
    CommandGateway commandGateway;

    @Test
    public void test() {
        try {
            commandGateway.send(new SimpleCommand("test command")).get();
            LOGGER.info("Command sent");
        } catch (Exception e) {
            LOGGER.info("Sending command FAILED with " + e);
            fail("Sending command FAILED with " + e);
        }
    }

    @Test
    public void test2() {
        try {
            commandGateway.send(new AnotherSimpleCommand("test command")).get();
            LOGGER.info("Command sent");
        } catch (Exception e) {
            LOGGER.info("Sending command FAILED with " + e);
            fail("Sending command FAILED with " + e);
        }
    }
}
