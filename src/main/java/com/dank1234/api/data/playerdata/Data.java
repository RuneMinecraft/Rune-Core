package com.dank1234.api.data.playerdata;

import java.io.Serializable;

public final class Data<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final K key;
    private final V value;

    private Data(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Data<K, V> of(K key, V value) {
        return new Data<>(key, value);
    }

    public K key() {
        return this.key;
    }

    public V value() {
        return this.value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Data<?, ?> data = (Data<?, ?>) obj;
        return key.equals(data.key) && value.equals(data.value);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
