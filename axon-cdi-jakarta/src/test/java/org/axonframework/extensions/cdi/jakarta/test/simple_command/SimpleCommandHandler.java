package org.axonframework.extensions.cdi.jakarta.test.simple_command;

import jakarta.inject.Inject;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.extensions.cdi.jakarta.annotations.ExternalCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExternalCommandHandler
public class SimpleCommandHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandler.class);

    @Inject
    EventGateway eventGateway;

    @CommandHandler
    public void handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
    }

    @CommandHandler
    public void handle(AnotherSimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
        eventGateway.publish("test");
    }
}
