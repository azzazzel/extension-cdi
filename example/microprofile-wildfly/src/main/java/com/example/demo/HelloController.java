package com.example.demo;

import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 */
@Path("/hello")
@Singleton
public class HelloController {

    @Inject
    CommandGateway commandGateway;

    @GET
    public String sayHello() {
        return "Hello World";
    }

    @GET
    @Path("/command")
    public String command() {
        if (commandGateway == null) {
            return "No command gateway :( " + UUID.randomUUID();
        } else {
            try {
                return (String)commandGateway.send(GenericCommandMessage.asCommandMessage("test message")).get();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }
}
