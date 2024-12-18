package com.dank1234.api.data.playerdata;

public class DataManager<T> {
    private final T type;
    public T type() {
        return this.type;
    }

    DataManager(T type) {
        this.type = type;
    }
    public static <T> DataManager<T> of(T type) {
        return new DataManager<>(type);
    }
}
