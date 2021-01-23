package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

import org.axonframework.queryhandling.QueryHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@QueryHandler
public @interface MetaQueryHandlerAnnotation {

    String queryName() default "metaQuery";

}
