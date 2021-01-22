package org.axonframework.extensions.cdi.jakarta;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.annotation.ParameterResolver;

import java.lang.reflect.Type;

public class CdiParameterResolver implements ParameterResolver<Object> {

    private final BeanManager beanManager;
    private final Bean<?> bean;
    private final Type type;

    public CdiParameterResolver(final BeanManager beanManager,
            final Bean<?> bean, final Type type) {
        this.beanManager = beanManager;
        this.bean = bean;
        this.type = type;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object resolveParameterValue(final Message message) {
        return beanManager.getReference(bean, type, beanManager.createCreationalContext(bean));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean matches(final Message message) {
        return true;
    }
}
