package com.example.minimoneybox.model.storage.impl;

import com.example.minimoneybox.model.storage.KeyValueStorage;
import com.orhanobut.hawk.Hawk;

public class HawkStorage implements KeyValueStorage {
    @Override
    public <T> boolean put(String key, T value) {
        return Hawk.put(key, value);
    }

    @Override
    public <T> T get(String key) {
        return Hawk.get(key);
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    @Override
    public long count() {
        return Hawk.count();
    }

    @Override
    public boolean deleteAll() {
        return Hawk.deleteAll();
    }

    @Override
    public boolean delete(String key) {
        return Hawk.delete(key);
    }

    @Override
    public boolean contains(String key) {
        return Hawk.contains(key);
    }
}
