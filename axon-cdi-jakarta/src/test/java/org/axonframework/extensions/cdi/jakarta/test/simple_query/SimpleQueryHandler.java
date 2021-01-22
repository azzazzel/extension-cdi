package org.axonframework.extensions.cdi.jakarta.test.simple_query;

import org.axonframework.queryhandling.QueryHandler;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;

public class SimpleQueryHandler {

    @QueryHandler
    public SimpleQueryResult handle(SimpleQuery query, SimpleInjectable simpleInjectable) {
        simpleInjectable.test();
        return new SimpleQueryResult(echo(query.getQuery()));
    }
}
