package com.dank1234.utils.data;

import com.dank1234.plugin.Main;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

public class NamespaceKey {
    static Map<String, NamespacedKey> keys = new HashMap<>();
    public static NamespacedKey get(String key) {
        return keys.computeIfAbsent(key, a -> new NamespacedKey(Main.get(), key));
    }
}
