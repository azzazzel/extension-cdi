package org.axonframework.extensions.cdi.ext.jakarta;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.*;
import jakarta.enterprise.util.AnnotationLiteral;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.config.DefaultConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Main CDI extension class responsible for collecting CDI beans and setting up
 * Axon configuration.
 */
public class AxonCdiExtension implements Extension {

    private static Logger LOGGER = LoggerFactory.getLogger(AxonCdiExtension.class);

    static {

        System.out.println("\n\n AxonCdiExtension \n\n");

        LOGGER.info("Axon CDI Extension V2 loaded");
    }

    private Set<Class> commandHandlerClasses = new HashSet<>();

    public void beforeBeanDiscovery (@Observes final BeforeBeanDiscovery bbdEvent) {
        LOGGER.debug("beforeBeanDiscovery");

    }

    void processAnnotatedType(@Observes ProcessAnnotatedType<?> patEvent) {
        LOGGER.debug("processAnnotatedType " + patEvent.getAnnotatedType());
    }

    <T> void observeForCommandHandlers(@Observes
                       @WithAnnotations(CommandHandler.class) final ProcessAnnotatedType<T> event) {

        LOGGER.debug("found command handler: " + event.getAnnotatedType().getJavaClass());

        commandHandlerClasses.add(event.getAnnotatedType().getJavaClass());

    }

    void afterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery,
                            final BeanManager beanManager) {
        LOGGER.debug("Configuring Axon Framework");

        afterBeanDiscovery.addBean()
                .types(Configuration.class)
                .qualifiers(new AnnotationLiteral<Default>() {}, new AnnotationLiteral<Any>() {})
                .scope(ApplicationScoped.class)
                .name(Configuration.class.getName())
                .beanClass(Configuration.class)
                .createWith(context -> {
//                    Configurer configurer = beanManager.createInstance()
//                            .select(Configurer.class, new AnnotationLiteral<Internal>() {}).get();
                    Configurer configurer = DefaultConfigurer.defaultConfiguration();
                    registerCommandHandlers(beanManager, configurer);

                    Configuration configuration = configurer.buildConfiguration();
//                    configuration.start();
                    return configuration;
                });

    }

    void afterDeploymentValidation(@Observes final AfterDeploymentValidation afterDeploymentValidation,
                                   final BeanManager beanManager) {
        LOGGER.debug("Starting Axon Framework");

        Configuration configuration = beanManager.createInstance()
                .select(Configuration.class,
                        new AnnotationLiteral<Default>() {},
                        new AnnotationLiteral<Any>() {}
                        ).get();

        configuration.onStart(() ->
                LOGGER.debug("Axon Framework Started!")
                );
        configuration.start();
    }

    private void registerCommandHandlers(BeanManager beanManager, Configurer configurer) {
        commandHandlerClasses.forEach(commandHandlerClass -> {
            Object commandHandler = getInstance(beanManager, commandHandlerClass);
            LOGGER.debug("Registering " + commandHandler + " with AxonFramework");
            configurer.registerCommandHandler(conf -> commandHandler);
        });
    }

    private <T> T getInstance(BeanManager beanManager, Class<T> beanClass) {
        T commandHandler = beanManager.createInstance()
                .select(beanClass,
                        new AnnotationLiteral<Default>() {},
                        new AnnotationLiteral<Any>() {}
                        ).get();
        return commandHandler;
    }

}
