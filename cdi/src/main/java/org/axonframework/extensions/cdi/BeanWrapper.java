package org.axonframework.extensions.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;
import jakarta.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import org.axonframework.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanWrapper<T> implements Bean<T>, PassivationCapable {

    private static final Logger logger = LoggerFactory.getLogger(
            MethodHandles.lookup().lookupClass());

    private final String name;
    private final Class<T> clazz;
    private final Supplier<T> supplier;

    public BeanWrapper(final Class<T> clazz, final Supplier<T> supplier) {
        this(clazz.getSimpleName(), clazz, supplier);
    }

    public BeanWrapper(final String name, final Class<T> clazz, final Supplier<T> supplier) {
        this.name = name;
        this.clazz = clazz;
        this.supplier = supplier;
    }

    @Override
    public T create(final CreationalContext<T> context) {
        return this.supplier.get();
    }

    @Override
    // Mark: Is this correct? Should I be doing something additional?
    public void destroy(T instance, final CreationalContext<T> context) {
        if (clazz.equals(Configuration.class)) {
            logger.info("Shutting down Axon configuration.");
            ((Configuration) instance).shutdown();
        }

        instance = null;
        context.release();
    }

    @Override
    public String getName() {
        return name;
    }

    @SuppressWarnings("serial")
    @Override
    public Set<Annotation> getQualifiers() {
        final Set<Annotation> qualifiers = new HashSet<>();

        qualifiers.add(new AnnotationLiteral<Default>() {
        });
        qualifiers.add(new AnnotationLiteral<Any>() {
        });

        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<Type> getTypes() {
        final Set<Type> types = new HashSet<>();

        types.add(clazz);
        types.add(Object.class);

        return types;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    // Mark: Is this sufficiently unique? Will this wrapper actually ever
    // get serialized? Is this supposed to be the ID of the underlying
    // bean instance or just this bean definition?
    public String getId() {
        return clazz.toString() + "#" + supplier.toString();
    }
}
