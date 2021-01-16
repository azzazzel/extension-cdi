package org.axonframework.extensions.cdi.jakarta.test.simple_command;

import jakarta.inject.Named;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.extensions.cdi.jakarta.annotations.ExternalCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ExternalCommandHandler
public class SimpleCommandHandler2 {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandler2.class);

    @CommandHandler
    public void handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
    }
}
