package org.axonframework.extension.example.swarm.query;

import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.extension.example.swarm.api.CustomerCreatedEvent;

@Named
public class CustomerEventListener {

  @PersistenceContext(name = "MyPU")
  private EntityManager em;

  @EventHandler
  public void on(CustomerCreatedEvent event) {
    em.persist(new CustomerView(event.getCustomerId(), event.getFullName(), event.getAge()));
  }
}
