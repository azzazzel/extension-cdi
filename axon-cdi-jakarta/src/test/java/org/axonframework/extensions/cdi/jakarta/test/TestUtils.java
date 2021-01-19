package org.axonframework.extensions.cdi.jakarta.test;

public class TestUtils {

    public static String echo (String string) {
        return "Echo " + string;
    }

    public static ThreadLocal<Boolean> success = new ThreadLocal<>();

}
