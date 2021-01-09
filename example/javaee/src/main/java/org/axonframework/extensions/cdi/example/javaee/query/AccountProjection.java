package org.axonframework.extensions.cdi.example.javaee.query;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.extensions.cdi.example.javaee.command.AccountCreatedEvent;

import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class AccountProjection {
    
    @Inject
    private EventBus eventBus;
            
    private static final Logger logger = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());

    @EventHandler
    public void on(AccountCreatedEvent event) {
        logger.log(Level.INFO, "Event Bus: {0}.", eventBus);
        logger.log(Level.INFO, "Projecting: {0}.", event);
    }
}
