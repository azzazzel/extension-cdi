package org.axonframework.extensions.cdi.jakarta.test.simple_command;

public class SimpleCommand {

    private final String command;

    public SimpleCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "SimpleCommand{" +
                "command='" + command + '\'' +
                '}';
    }
}
