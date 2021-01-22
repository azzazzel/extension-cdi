package org.axonframework.extensions.cdi.jakarta.test.simple_query;

import org.axonframework.queryhandling.QueryHandler;

public class SimpleQueryHandler {

    @QueryHandler
    public SimpleQueryResult handle(SimpleQuery query) {
        return new SimpleQueryResult("Response to " + query.getQuery());
    }
}
