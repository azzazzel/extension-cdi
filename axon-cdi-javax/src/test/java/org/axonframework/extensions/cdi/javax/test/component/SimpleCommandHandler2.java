package org.axonframework.extensions.cdi.javax.test.component;

import org.axonframework.commandhandling.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named
public class SimpleCommandHandler2 {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandler2.class);

    @CommandHandler
    public void handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
    }
}
