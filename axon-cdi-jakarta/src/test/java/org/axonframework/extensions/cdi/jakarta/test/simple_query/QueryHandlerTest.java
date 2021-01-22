package org.axonframework.extensions.cdi.jakarta.test.simple_query;

import jakarta.inject.Inject;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ArquillianExtension.class)
public class QueryHandlerTest {

    private static Logger LOGGER = LoggerFactory.getLogger(QueryHandlerTest.class);

    @Deployment
    public static WebArchive createDeployment () {
        WebArchive archive = ArchiveTemplates.webArchiveWithCdiExtension();
        archive
                .addPackage(QueryHandlerTest.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));
        return archive;
    }

    @Inject
    QueryGateway queryGateway;

    private String message = "test command";

    @Test
    public void test() {
        try {
            String result = (String) queryGateway.query(new SimpleQuery(message), SimpleQueryResult.class).get().getText();
//            assertEquals(echo(message), result);
//            result = (String) queryGateway.send(new AnotherSimpleCommand(message)).get();
//            assertEquals(echo(message), result);
        } catch (Exception e) {
            fail("Sending command FAILED with " + e);
        }
    }
}
