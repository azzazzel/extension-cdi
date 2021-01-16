package com.example.demo;

/*
    Adding the @Aggregate annotation causes container to fail.
    The error is "One of the start handlers in phase [null] failed with the following exception"
    Perhaps related to https://github.com/AxonFramework/AxonFramework/issues/1482
 */


import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.extensions.cdi.javax.annotations.ExternalCommandHandler;

import javax.enterprise.context.ApplicationScoped;

@ExternalCommandHandler
@ApplicationScoped
public class SimpleCommandHandler {

    @CommandHandler
    public String doSomething (String command) {
        System.out.println("Handling command: " + command);
        return ("Handled command: " + command);
    }
}
