package org.axonframework.extensions.cdi.jakarta.annotations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Stereotype;

@ApplicationScoped
@Default
@Stereotype
public @interface AxonConfig {
}
