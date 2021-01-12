package org.axonframework.extensions.cdi.ext.javax.test.component;

import org.axonframework.commandhandling.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
