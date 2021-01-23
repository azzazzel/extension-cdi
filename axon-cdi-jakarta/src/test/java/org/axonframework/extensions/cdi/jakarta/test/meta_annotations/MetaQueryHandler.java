package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.echo;

public class MetaQueryHandler {

    @MetaQueryHandlerAnnotation
    public SimpleQueryResult handle(SimpleQuery query) {
        return new SimpleQueryResult(echo(query.getQuery()));
    }
}
