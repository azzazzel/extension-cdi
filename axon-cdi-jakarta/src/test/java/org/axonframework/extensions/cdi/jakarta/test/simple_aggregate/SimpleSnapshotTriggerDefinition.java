package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.AbstractSnapshotTrigger;
import org.axonframework.eventsourcing.SnapshotTrigger;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.successes;

@Named
public class SimpleSnapshotTriggerDefinition implements SnapshotTriggerDefinition {

    @Inject
    Configuration configuration;

    @Override
    public SnapshotTrigger prepareTrigger(Class<?> aggregateType) {
        successes.get().put("snapshotTriggerDefinition", true);
        return new MySnapshotTrigger(aggregateType);
    }

    private class MySnapshotTrigger extends AbstractSnapshotTrigger {

        public MySnapshotTrigger(Class<?> aggregateType) {
            super(configuration.snapshotter(), aggregateType);
        }

        @Override
        protected boolean exceedsThreshold() {
            return true;
        }

        @Override
        protected void reset() {
        }
    }
}
