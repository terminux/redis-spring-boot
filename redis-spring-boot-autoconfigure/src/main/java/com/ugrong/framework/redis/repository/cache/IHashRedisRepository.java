package com.ugrong.framework.redis.repository.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IHashRedisRepository<K, V extends Serializable> extends IRedisObjectRepository<V> {

    void put(String keySuffix, K field, V value);

    void putWithDefaultTimeout(String keySuffix, K field, V value);

    void putWithTimeout(String keySuffix, K field, V value, long timeout, TimeUnit timeUnit);

    void putAll(String keySuffix, Map<? extends K, ? extends V> map);

    void putAllWithDefaultTimeout(String keySuffix, Map<? extends K, ? extends V> map);

    void putAllWithTimeout(String keySuffix, Map<? extends K, ? extends V> map, long timeout, TimeUnit timeUnit);

    void remove(String keySuffix, K field);

    Optional<V> get(String keySuffix, K field);

    List<V> getAll(String keySuffix);

    Boolean hasField(String keySuffix, K field);

    Set<K> fields(String keySuffix);

    Map<K, V> entries(String keySuffix);

    Long getAndIncrement(String keySuffix, K field);

    Long getAndIncrementBy(String keySuffix, K field, Long incrementBy);
}
