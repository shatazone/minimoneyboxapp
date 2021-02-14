package com.example.minimoneybox.model.storage.impl;

import com.example.minimoneybox.misc.JsonSerializer;
import com.example.minimoneybox.model.storage.KeyValueStorage;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class InMemoryMapStorage implements KeyValueStorage {
    private final Map<String, String> map = new HashMap<>();
    private final JsonSerializer serializer;

    @Inject
    public InMemoryMapStorage(JsonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T> boolean put(String key, T value) {
        map.put(key, serializer.toJson(value));
        return true;
    }

    @Override
    public <T> T get(String key) {
        return (T) serializer.fromJson(map.get(key));
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        return (T) (map.containsKey(key) ? get(key) : defaultValue);
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public boolean deleteAll() {
        map.clear();
        return true;
    }

    @Override
    public boolean delete(String key) {
        return map.remove(key) != null;
    }

    @Override
    public boolean contains(String key) {
        return map.containsKey(key);
    }
}
