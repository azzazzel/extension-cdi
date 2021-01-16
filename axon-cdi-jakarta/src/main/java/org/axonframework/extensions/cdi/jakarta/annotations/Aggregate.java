package org.axonframework.extensions.cdi.jakarta.annotations;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Stereotype
@Dependent
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Aggregate {
}
