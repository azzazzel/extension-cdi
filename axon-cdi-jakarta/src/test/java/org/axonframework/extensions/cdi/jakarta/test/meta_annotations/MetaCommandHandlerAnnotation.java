package org.axonframework.extensions.cdi.jakarta.test.meta_annotations;

import org.axonframework.commandhandling.CommandHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@CommandHandler(payloadType = SimpleCommand.class)
public @interface MetaCommandHandlerAnnotation {

    String commandName();

    String routingKey() default "";
}
