package pro.sholokhov.storage;

import java.util.Optional;

public interface KVStorage<K, V> {

  Optional<V> get(K key);

  void put(K key, V value);

  Optional<V> remove(K key);

}
