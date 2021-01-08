package com.example.demo;

import org.axonframework.queryhandling.QueryGateway;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path("/hello")
@Singleton
public class HelloController {

    @Inject
    QueryGateway queryGateway;

    @GET
    public String sayHello() {
        return "Hello World";
    }

    @GET
    @Path("/query")
    public String command() {
        return "QueryGateway injected: " + queryGateway;
    }

}
