package org.axonframework.extensions.cdi.v2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.*;
import jakarta.enterprise.util.AnnotationLiteral;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.extensions.cdi.v2.qualifier.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main CDI extension class responsible for collecting CDI beans and setting up
 * Axon configuration.
 */
public class AxonCdiExtension implements Extension {

    private static Logger LOGGER = LoggerFactory.getLogger(AxonCdiExtension.class);

    static {
        LOGGER.info("Axon CDI Extension V2 loaded");
    }

    public void beforeBeanDiscovery (@Observes final BeforeBeanDiscovery bbdEvent) {
        LOGGER.debug("beforeBeanDiscovery");

    }

    void processAnnotatedType(@Observes ProcessAnnotatedType<?> patEvent) {
        LOGGER.debug("processAnnotatedType " + patEvent.getAnnotatedType());
    }


    void afterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery,
                            final BeanManager beanManager) {
        LOGGER.debug("Starting Axon Framework configuration.");

        afterBeanDiscovery.addBean()
                .types(Configuration.class)
                .qualifiers(new AnnotationLiteral<Default>() {}, new AnnotationLiteral<Any>() {})
                .scope(ApplicationScoped.class)
                .name(Configuration.class.getName())
                .beanClass(Configuration.class)
                .createWith(context -> {
                    Configurer configurer = beanManager.createInstance()
                            .select(Configurer.class, new AnnotationLiteral<Internal>() {}).get();
                    Configuration configuration = configurer.buildConfiguration();
                    configuration.start();
                    return configuration;
                });

    }

}
