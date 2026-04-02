package com.api.framework.world;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-test-instance world object to store test-only context.
 *
 * Added a test case that makes use of it (and rightfully fails, since mocked API we use is randomizing data -
 * so data from two service calls don't match.
 */
@Component
@Scope("prototype")
public class ScenarioWorld {

    private final Map<String, Object> context = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        return (T) context.get(key);
    }
}
