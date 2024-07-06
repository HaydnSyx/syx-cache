package cn.syx.cache.struct.skip;

public interface SkipList<K, T> {

    void add(K key, T value);

    T remove(K key);

    void set(K key, T value);

    T get(K key);

    boolean contains(K key);

    boolean isEmpty();

    int getSize();
}
