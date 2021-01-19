package org.axonframework.extensions.cdi.jakarta.test;

import java.util.Map;

public class TestUtils {

    public static String echo (String string) {
        return "Echo " + string;
    }

    public static ThreadLocal<Boolean> success = new ThreadLocal<>();

    public static ThreadLocal<Map<String,Boolean>> successes = new ThreadLocal<>();

}
