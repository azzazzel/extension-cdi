package org.axonframework.extensions.cdi.jakarta.test.component;

public class SimpleCommand {

    private String command;

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
