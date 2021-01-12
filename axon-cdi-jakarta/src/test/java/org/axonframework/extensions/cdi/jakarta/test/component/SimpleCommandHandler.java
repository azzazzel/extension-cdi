package org.axonframework.extensions.cdi.jakarta.test.component;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.extensions.cdi.jakarta.ExternalCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExternalCommandHandler
public class SimpleCommandHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandler.class);

    @CommandHandler
    public void handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
    }

    @CommandHandler
    public void handle(AnotherSimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
    }
}
