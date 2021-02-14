package com.example.minimoneybox.model.storage;

public interface KeyValueStorage {
    <T> boolean put(String key, T value);

    <T> T get(String key);

    <T> T get(String key, T defaultValue);

    long count();

    boolean deleteAll();

    boolean delete(String key);

    boolean contains(String key);
}
