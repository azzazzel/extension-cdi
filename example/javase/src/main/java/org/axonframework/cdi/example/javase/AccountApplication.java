package org.axonframework.extensions.cdi.example.javase;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Inject;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.interceptors.EventLoggingInterceptor;
import org.axonframework.extensions.cdi.example.javase.command.CreateAccountCommand;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class AccountApplication {

    @Inject
    private EventBus eventBus;

    @Inject
    private CommandGateway commandGateway;

    @Inject
    private QueryGateway queryGateway;

    public void run() {
        eventBus.registerDispatchInterceptor(new EventLoggingInterceptor());
        commandGateway.sendAndWait(new CreateAccountCommand("4711", 1000D));
        queryGateway.scatterGather("4711", ResponseTypes.instanceOf(Double.class), 1, TimeUnit.SECONDS)
                .forEach(System.out::println);
    }

    public static void main(final String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();

        try (SeContainer container = initializer.initialize()) {
            container.select(AccountApplication.class).get().run();
        }
    }
}
