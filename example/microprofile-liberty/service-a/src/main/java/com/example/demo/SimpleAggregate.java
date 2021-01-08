package com.example.demo;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.extensions.cdi.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.UUID;

/*
    Adding the @Aggregate annotation causes container to fail.
    The error is "One of the start handlers in phase [null] failed with the following exception"
    Perhaps related to https://github.com/AxonFramework/AxonFramework/issues/1482
 */


//@Aggregate
public class SimpleAggregate {

//    @AggregateIdentifier UUID id;
//
//    @CommandHandler
//    void doSomething (String command) {
//        System.out.println("Handling command: " + command);
//    }
}
