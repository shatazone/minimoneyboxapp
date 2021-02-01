package com.example.minimoneybox.model.storage;

import com.example.minimoneybox.misc.JsonSerializer;
import com.example.minimoneybox.model.storage.impl.InMemoryMapStorage;
import com.google.gson.Gson;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

public class InMemoryMapStorageTest {

    private KeyValueStorage storage;

    @Before
    public void setup() {
        this.storage = createInMemoryStorage();
    }

    @Test
    public void testCountWorksWithPrimitiveTypes() {
        storage.put("key1", 1);
        storage.put("key2", 2);
        storage.put("key3", 3);

        Assertions.assertThat(storage.contains("key1")).isTrue();
        Assertions.assertThat(storage.count()).isEqualTo(3);

        storage.deleteAll();
        Assertions.assertThat(storage.contains("key1")).isFalse();
        Assertions.assertThat(storage.count()).isEqualTo(0);
    }

    @Test
    public void testCountWorksWithObjectTypes() {
        storage.put("key1", new Wrapper(1));
        storage.put("key2", new Wrapper(2));

        Assertions.assertThat(storage.contains("key1")).isTrue();
        Assertions.assertThat(storage.count()).isEqualTo(3);

        storage.deleteAll();
        Assertions.assertThat(storage.contains("key1")).isFalse();
        Assertions.assertThat(storage.count()).isEqualTo(0);
    }

    @Test
    public void testInsertedAndRetrievedObjectsAreCopies() {
        Wrapper expected = new Wrapper(1);
        storage.put("key1", expected);
        Wrapper actual = storage.get("key1");

        Assertions.assertThat(actual)
                .isEqualTo(expected)
                .withFailMessage("Inserted and retrieved values are not equivalent");

        Assertions.assertThat(actual == expected)
                .isEqualTo(false)
                .withFailMessage("The same object inserted into storage is being retrieved");
    }

    @Test
    public void testInsertedAndRetrievedPrimitivesAreTheSame() {
        int expected = 1;
        storage.put("key1", expected);
        int actual = storage.get("key1");

        Assertions.assertThat(actual)
                .isEqualTo(expected)
                .withFailMessage("Inserted and retrieved values are not equivalent");

        Assertions.assertThat(actual == expected)
                .isEqualTo(true)
                .withFailMessage("Primitives inserted and retieved are not the same");
    }

    @Data
    @AllArgsConstructor
    public class Wrapper {
        private int value;
    }

    private KeyValueStorage createInMemoryStorage() {
        return new InMemoryMapStorage(new JsonSerializer(new Gson()));
    }
}