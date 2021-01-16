package org.axonframework.extensions.cdi.common;

import org.axonframework.common.AxonConfigurationException;
import org.axonframework.common.Registration;
import org.axonframework.common.stream.BlockingStream;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventhandling.TrackingToken;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.MessageDispatchInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MissingEventStore implements EventStore {

    private final String message;

    public MissingEventStore(String message) {
        this.message = message;
    }

    @Override
    public DomainEventStream readEvents(String aggregateIdentifier) {
        throw new AxonConfigurationException(message);
    }


    @Override
    public DomainEventStream readEvents(String aggregateIdentifier, long firstSequenceNumber) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public void storeSnapshot(DomainEventMessage<?> snapshot) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public Optional<Long> lastSequenceNumberFor(String aggregateIdentifier) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public void publish(EventMessage<?>... events) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public void publish(List<? extends EventMessage<?>> events) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public Registration registerDispatchInterceptor(MessageDispatchInterceptor<? super EventMessage<?>> dispatchInterceptor) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public Registration subscribe(Consumer<List<? extends EventMessage<?>>> messageProcessor) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public BlockingStream<TrackedEventMessage<?>> openStream(TrackingToken trackingToken) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public TrackingToken createTailToken() {
        throw new AxonConfigurationException(message);
    }

    @Override
    public TrackingToken createHeadToken() {
        throw new AxonConfigurationException(message);
    }

    @Override
    public TrackingToken createTokenAt(Instant dateTime) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public TrackingToken createTokenSince(Duration duration) {
        throw new AxonConfigurationException(message);
    }

}
