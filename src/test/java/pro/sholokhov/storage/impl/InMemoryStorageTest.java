package pro.sholokhov.storage.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pro.sholokhov.storage.KVStorage;

import java.util.Optional;

/**
 * @author Artem Sholokhov
 */
public class InMemoryStorageTest {

  private static KVStorage<String, Integer> storage;

  @BeforeClass
  public static void setUp() throws Exception {
    storage = new InMemoryStorage<>();
  }

  @Test
  public void get() throws Exception {
    storage.put("one", 1);
    storage.put("two", 2);
    storage.put("three", 3);

    Integer v = storage.get("three").orElseThrow(() ->
        new IllegalStateException("No value was presented in storage"));
    assertTrue("Invalid value", v == 3);

    storage.get("some").ifPresent(dummy -> {
      throw new IllegalStateException("Should be None instead of value!");
    });
  }

  @Test
  public void put() throws Exception {
    storage.put("UltimateAnswer", 42);
    Integer v = storage.get("UltimateAnswer").orElseThrow(() ->
        new IllegalStateException("No value was presented in storage"));
    assertTrue("Incorrect value for this key", v == 42);
  }

  @Test
  public void remove() throws Exception {
    storage.put("testRemove", 777);
    storage.remove("testRemove");
    storage.get("testRemove").ifPresent(dummy -> {
      throw new IllegalStateException("Value still presented in storage");
    });
  }

}