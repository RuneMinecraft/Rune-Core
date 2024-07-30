package com.dank1234.utils.data.playerdata;

import java.util.List;

public interface Persistable {
    List<Data<String, String>> getData();
    void setData(String key, Object value);
    void saveData();
    void loadData();
}