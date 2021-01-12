package org.axonframework.extensions.cdi.ext.jakarta.test.component;

import jakarta.inject.Named;
import org.axonframework.commandhandling.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class SimpleCommandHandler2 {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandler2.class);

    @CommandHandler
    public void handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
    }
}
