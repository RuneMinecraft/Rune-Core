package com.dank1234.utils.data.playerdata;

import java.io.Serializable;

public final class Data<K, V> implements Serializable {
    private K key;
    private V value;

    public K key() {
        return this.key;
    }
    public V value() {
        return this.value;
    }
    public K key(K key) {
        return this.key = key;
    }
    public V value(V value) {
        return this.value = value;
    }

    private Data(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public static <K, V> Data<K, V> of(K key, V value) {
        return new Data<>(key, value);
    }

    @Override
    public String toString() {
        return (String) value();
    }
}
