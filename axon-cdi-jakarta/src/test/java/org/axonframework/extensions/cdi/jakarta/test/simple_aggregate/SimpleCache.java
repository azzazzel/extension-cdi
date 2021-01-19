package org.axonframework.extensions.cdi.jakarta.test.simple_aggregate;

import jakarta.inject.Named;
import org.axonframework.common.Registration;
import org.axonframework.common.caching.Cache;

import java.io.Serializable;

import static org.axonframework.extensions.cdi.jakarta.test.TestUtils.successes;

@Named
public class SimpleCache implements Serializable, Cache {

    @Override
    public <K, V> V get(K key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        successes.get().put("cache", true);
    }

    @Override
    public boolean putIfAbsent(Object key, Object value) {
        return false;
    }

    @Override
    public boolean remove(Object key) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Registration registerCacheEntryListener(EntryListener cacheEntryListener) {
        return null;
    }
}
