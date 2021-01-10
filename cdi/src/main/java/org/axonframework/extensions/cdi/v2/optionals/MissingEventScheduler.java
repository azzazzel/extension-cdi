package org.axonframework.extensions.cdi.v2.optionals;

import org.axonframework.common.AxonConfigurationException;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventhandling.scheduling.ScheduleToken;

import java.time.Duration;
import java.time.Instant;

public class MissingEventScheduler implements EventScheduler {

    private final String message;

    public MissingEventScheduler(String message) {
        this.message = message;
    }

    @Override
    public ScheduleToken schedule(Instant triggerDateTime, Object event) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public ScheduleToken schedule(Duration triggerDuration, Object event) {
        throw new AxonConfigurationException(message);
    }

    @Override
    public void cancelSchedule(ScheduleToken scheduleToken) {
        throw new AxonConfigurationException(message);
    }
}
