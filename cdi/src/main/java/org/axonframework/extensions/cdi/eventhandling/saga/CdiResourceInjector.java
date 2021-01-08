package org.axonframework.extensions.cdi.eventhandling.saga;

import org.axonframework.modelling.saga.ResourceInjector;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionTarget;

/**
 * @author Milan Savic
 */
public class CdiResourceInjector implements ResourceInjector {

    private final BeanManager beanManager;

    public CdiResourceInjector(BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void injectResources(Object saga) {
        CreationalContext creationalContext = beanManager.createCreationalContext(null);

        AnnotatedType annotatedType = beanManager.createAnnotatedType(saga.getClass());
        InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotatedType);
        injectionTarget.inject(saga, creationalContext);
    }
}
