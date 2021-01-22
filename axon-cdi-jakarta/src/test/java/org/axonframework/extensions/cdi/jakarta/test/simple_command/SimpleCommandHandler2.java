package org.axonframework.extensions.cdi.jakarta.test.simple_command;

import jakarta.inject.Named;
import org.axonframework.commandhandling.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;

@Named
public class SimpleCommandHandler2 {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandler2.class);

    @CommandHandler
    public String handle(SimpleCommand cmd) {
        LOGGER.info("Handling command " + cmd);
        return echo(cmd.getCommand());
    }
}
