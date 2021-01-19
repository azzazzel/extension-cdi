package org.axonframework.extensions.cdi.jakarta.test.simple_command;

public class AnotherSimpleCommand {

    private String command;

    public AnotherSimpleCommand(String command) {
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
