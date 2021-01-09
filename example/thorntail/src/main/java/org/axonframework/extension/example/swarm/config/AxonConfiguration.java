package org.axonframework.extension.example.swarm.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;

@ApplicationScoped
public class AxonConfiguration {

  private static final String userTransaction = "java:comp/UserTransaction";

  @PersistenceContext(name = "MyPU")
  private EntityManager em;

//  @Produces
//  public TransactionManager transactionManager() {
//    return new ContainerTransactionManager(em, userTransaction);
//  }

//  @Produces
//  public EntityManagerProvider entityManagerProvider() {
//    return new SimpleEntityManagerProvider(em);
//  }

  @Produces
  public EventStorageEngine eventStorageEngine(final EntityManagerProvider entityManagerProvider, final TransactionManager transactionManager) {
    return JpaEventStorageEngine.builder()
            .entityManagerProvider(entityManagerProvider)
            .transactionManager(transactionManager)
            .build();
  }
}
