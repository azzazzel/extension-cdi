package org.axonframework.extensions.cdi.javax.test.component;

public class AnotherSimpleCommand {

    private String command;

    public AnotherSimpleCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "SimpleCommand{" +
                "command='" + command + '\'' +
                '}';
    }
}
