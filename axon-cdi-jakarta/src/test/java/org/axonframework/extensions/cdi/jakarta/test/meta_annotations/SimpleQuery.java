package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

public class SimpleQuery {

    private String query;

    public SimpleQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
