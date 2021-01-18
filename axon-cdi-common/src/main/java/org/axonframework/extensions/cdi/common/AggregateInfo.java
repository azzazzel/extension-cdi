package org.axonframework.extensions.cdi.common;

import org.axonframework.common.caching.Cache;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.snapshotting.SnapshotFilter;
import org.axonframework.modelling.command.CommandTargetResolver;
import org.axonframework.modelling.command.Repository;

public class AggregateInfo {

    public String beanName;
    public Class classType;

    public AggregateFactory aggregateFactory;
    public Cache cache;
    public CommandTargetResolver commandTargetResolver;
    public boolean filterEventsByType;
    public Repository repository;
    public SnapshotFilter snapshotFilter;
    public SnapshotTriggerDefinition snapshotTriggerDefinition;
    public String type;


    public AggregateInfo(String beanName, Class classType) {
        this.beanName = beanName;
        this.classType = classType;
    }

    @Override
    public String toString() {
        return "AggregateInfo{" +
                "beanName='" + beanName + '\'' +
                ", classType=" + classType +
                ", aggregateFactory=" + aggregateFactory +
                ", cache=" + cache +
                ", commandTargetResolver=" + commandTargetResolver +
                ", filterEventsByType=" + filterEventsByType +
                ", repository=" + repository +
                ", snapshotFilter=" + snapshotFilter +
                ", snapshotTriggerDefinition=" + snapshotTriggerDefinition +
                ", type='" + type + '\'' +
                '}';
    }
}
