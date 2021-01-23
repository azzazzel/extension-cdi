package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;

public class MetaCommandHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(MetaCommandHandler.class);

    @MetaCommandHandlerAnnotation(commandName = "testCommand")
    public String handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
        return echo(cmd.getCommand());
    }

}
