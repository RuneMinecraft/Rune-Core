package com.dank1234.utils.data.playerdata;

import java.util.List;

public interface Persistable {
    List<Data<String, String>> getAllData();
    <T> void setData(String key, T value);
    boolean hasKey(String key);
    void saveData();
    void loadData();
}