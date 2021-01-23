package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

import jakarta.inject.Inject;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.extensions.cdi.jakarta.test.ArchiveTemplates;
import org.axonframework.queryhandling.QueryGateway;
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
public class MetaAnnotationsTest {

    private static Logger LOGGER = LoggerFactory.getLogger(MetaAnnotationsTest.class);

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        archive
                .addPackage(MetaAnnotationsTest.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));
        return archive;
    }

    @Inject
    CommandGateway commandGateway;

    @Inject
    QueryGateway queryGateway;

    private String message = "test command";

    @Test
    public void testCommand() {
        try {
            CommandMessage<SimpleCommand> commandMessage = new GenericCommandMessage (
              GenericCommandMessage.asCommandMessage(new SimpleCommand(message)),
              "testCommand"
            );

            String result = (String) commandGateway.send(commandMessage).get();
            assertEquals(echo(message), result);
        } catch (Exception e) {
            fail("Sending command FAILED with " + e);
        }
    }

    @Test
    public void testQuery() {
        try {
            String result = (String) queryGateway.query(
                    "metaQuery",
                    new SimpleQuery(message),
                    SimpleQueryResult.class).get().getText();
            assertEquals(echo(message), result);
        } catch (Exception e) {
            fail("Sending command FAILED with " + e);
        }
    }
}
