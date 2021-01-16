import org.axonframework.extensions.cdi.common.AbstractAxonProducers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;


import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;

@Disabled
public class TransformationTest {

    @Test
    void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?> axonProducersClass = Class.forName("org.axonframework.extensions.cdi.javax.AxonProducers");

        Arrays.stream(axonProducersClass.getDeclaredFields()).forEach(field -> {
            System.out.println("Found field: " + field.getName());
        });

        Arrays.stream(axonProducersClass.getDeclaredMethods()).forEach(method ->
                System.out.println("Found method: " + method)
        );

        AbstractAxonProducers axonProducers = (AbstractAxonProducers) axonProducersClass.newInstance();

        axonProducers.configuration();

    }

    @Test
    void test2() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("javax.enterprise.inject.Instance");

        Arrays.stream(clazz.getMethods()).forEach(field ->
                System.out.println("Method found: " + field)
        );

    }

}
