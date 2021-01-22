package org.axonframework.extensions.cdi.jakarta.test.simple_query;

public class SimpleQuery {

    private String query;

    public SimpleQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
