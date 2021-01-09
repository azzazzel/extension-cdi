package org.axonframework.extensions.cdi.example.javaee.query;

import jakarta.enterprise.context.ApplicationScoped;
import org.axonframework.eventhandling.EventHandler;

import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class GenericEventListener {

    private static final Logger logger = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());

    @EventHandler
    public void on(Object event) {
        logger.log(Level.INFO, "Received: {0}.", event);
    }
}
