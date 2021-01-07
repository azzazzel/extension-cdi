package org.axonframework.extension.example.swarm.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@Getter
public class CreateCustomerCommand {

  @TargetAggregateIdentifier
  private final String customerId;

  private final String fullName;
  private final Integer age;
}
