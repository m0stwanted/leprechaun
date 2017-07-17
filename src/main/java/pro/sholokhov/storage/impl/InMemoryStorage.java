package pro.sholokhov.storage.impl;

import pro.sholokhov.storage.KVStorage;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage<K, V> implements KVStorage<K, V> {

  private final Map<K, V> repo = new ConcurrentHashMap<>();

  @Override
  public Optional<V> get(K key) {
    Objects.requireNonNull(key);
    return Optional.ofNullable(repo.get(key));
  }

  @Override
  public void put(K key, V value) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    repo.put(key, value);
  }

  @Override
  public void remove(K key) {
    Objects.requireNonNull(key);
    repo.remove(key);
  }

}
