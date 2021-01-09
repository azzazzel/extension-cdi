package org.axonframework.extensions.cdi.example.javaee;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.interceptors.EventLoggingInterceptor;
import org.axonframework.extensions.cdi.example.javaee.command.CreateAccountCommand;

import java.lang.invoke.MethodHandles;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
@Startup
public class AccountApplication {

    private static final Logger logger = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    private EventBus eventBus;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    private CommandGateway commandGateway;

    @PostConstruct
    public void run() {
        logger.info("Initializing Account application.");

        eventBus.registerDispatchInterceptor(new EventLoggingInterceptor());

        commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(), 100.00));
    }
}
