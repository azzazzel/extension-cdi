package org.axonframework.extensions.cdi.jakarta.test.simple_query;

import org.axonframework.queryhandling.QueryHandler;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;

public class SimpleQueryHandler {

    @QueryHandler
    public SimpleQueryResult handle(SimpleQuery query) {
        return new SimpleQueryResult(echo(query.getQuery()));
    }
}
