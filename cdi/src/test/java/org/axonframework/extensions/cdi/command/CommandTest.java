package org.axonframework.extensions.cdi.command;

import jakarta.enterprise.context.ApplicationScoped;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.extensions.cdi.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@ExtendWith(ArquillianExtension.class)
public class CommandTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

//    @Rule
//    public WeldInitiator weld = WeldInitiator
//            .from(CommandTest.Account.class)
//            .inject(this)
//            .build();

    private AggregateTestFixture<Account> account;

    @BeforeEach
    public void setup() {
        account = new AggregateTestFixture<>(Account.class);
    }

    @Test
    public void testAccountCreate() {
        account.givenNoPriorActivity()
               .when(new CreateAccountCommand("4711", 100.00))
               .expectEvents(new AccountCreatedEvent("4711", 100.00));
    }

    public static class CreateAccountCommand {

        @TargetAggregateIdentifier
        private final String accountId;
        private final Double overdraftLimit;

        public CreateAccountCommand(String accountId, Double overdraftLimit) {
            this.accountId = accountId;
            this.overdraftLimit = overdraftLimit;
        }

        public String getAccountId() {
            return accountId;
        }

        public Double getOverdraftLimit() {
            return overdraftLimit;
        }

        @Override
        public String toString() {
            return "Create Account Command with accountId=" + accountId
                    + ", overdraftLimit=" + overdraftLimit;
        }
    }

    public static class AccountCreatedEvent {

        private final String accountId;
        private final Double overdraftLimit;

        public AccountCreatedEvent(String accountId, Double overdraftLimit) {
            this.accountId = accountId;
            this.overdraftLimit = overdraftLimit;
        }

        public String getAccountId() {
            return accountId;
        }

        public Double getOverdraftLimit() {
            return overdraftLimit;
        }

        @Override
        public String toString() {
            return "Account Created Event with accountId=" + accountId
                    + ", overdraftLimit=" + overdraftLimit;
        }
    }

    @ApplicationScoped
    @Aggregate
    public static class Account {

        @AggregateIdentifier
        private String accountId;

        @SuppressWarnings("unused")
        private Double overdraftLimit;

        public Account() {
            // Empty constructor needed for CDI proxying.
        }

        @CommandHandler
        public Account(final CreateAccountCommand command) {
            apply(new AccountCreatedEvent(command.getAccountId(),
                                          command.getOverdraftLimit()));
        }

        @EventSourcingHandler
        public void on(AccountCreatedEvent event) {
            this.accountId = event.getAccountId();
            this.overdraftLimit = event.getOverdraftLimit();
        }
    }
}
