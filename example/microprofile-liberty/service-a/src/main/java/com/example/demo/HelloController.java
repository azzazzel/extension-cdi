package com.example.demo;

import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
@Path("/hello")
@Singleton
public class HelloController {

    static {

        System.out.println("\n\n HelloController \n\n");

    }


    @Inject
    CommandGateway commandGateway;

    @GET
    public String sayHello() {
        return "Hello World";
    }

    @GET
    @Path("/command")
    public CompletableFuture<String> command() {

        return commandGateway.send(GenericCommandMessage.asCommandMessage("test message"));
    }

}
