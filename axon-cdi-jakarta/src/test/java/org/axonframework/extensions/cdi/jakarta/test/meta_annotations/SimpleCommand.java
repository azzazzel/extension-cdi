package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

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
