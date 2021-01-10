package org.axonframework.extensions.cdi.example.javase.query;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.extensions.cdi.example.javase.command.AccountCreatedEvent;
import org.axonframework.queryhandling.QueryHandler;

import javax.enterprise.context.ApplicationScoped;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class AccountProjection {

    private Map<String, Double> accounts = new HashMap<>();

    private static final Logger logger = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());

    @EventHandler
    public void on(AccountCreatedEvent event) {
        logger.log(Level.INFO, "Projecting: {0}.", event);
        accounts.put(event.getAccountId(), event.getOverdraftLimit());
    }

    @QueryHandler
    public Double query(String accountId) {
        logger.log(Level.INFO, "Querying: {0}.", accountId);
        return accounts.get(accountId);
    }
}