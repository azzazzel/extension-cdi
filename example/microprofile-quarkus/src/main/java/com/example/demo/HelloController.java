package com.example.demo;

import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    public String command() throws ExecutionException, InterruptedException {
        return (String) commandGateway.send(GenericCommandMessage.asCommandMessage("test message")).get();
    }

}
