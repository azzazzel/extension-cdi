package org.axonframework.extensions.cdi.jakarta.test.simple_command;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;

public class SimpleCommand {

    private final String command;

    public SimpleCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "SimpleCommand{" +
                "command='" + command + '\'' +
                '}';
    }
}
