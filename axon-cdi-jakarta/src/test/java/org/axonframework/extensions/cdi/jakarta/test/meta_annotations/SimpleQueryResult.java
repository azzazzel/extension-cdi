package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

public class SimpleQueryResult {

    private String text;

    public SimpleQueryResult(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
