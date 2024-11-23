package com.dank1234.utils.data;

import com.dank1234.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public final class Config {
    private static final List<String> CONFIG_LOCATIONS = List.of(
            "plugins/Rune-Core/config.yml",
            "config/Rune-Core/config.yml"
    );

    private static final ReentrantLock lock = new ReentrantLock();

    private Map<String, Object> configData = new HashMap<>();
    private File loadedConfigFile;

    private Config() {}

    private static final class InstanceHolder {
        private static final Config instance = new Config();
    }

    /**
     * Gets the singleton instance of the Config class.
     */
    public static Config get() {
        return InstanceHolder.instance;
    }

    /**
     * Loads the configuration file into memory.
     */
    public void loadConfig() {
        lock.lock();
        try {
            loadedConfigFile = findConfigFile().orElse(null);

            if (loadedConfigFile == null) {
                Logger.log("[Config] No config file found.");
                return;
            }

            try (InputStream input = new FileInputStream(loadedConfigFile)) {
                Yaml yaml = new Yaml();

                // Parse the YAML file into a map
                Map<String, Object> loadedMap = yaml.load(input);
                configData = (loadedMap != null) ? loadedMap : new HashMap<>();

                Logger.log("[Config] Config file loaded successfully: " + loadedConfigFile.getPath());
            } catch (IOException e) {
                Logger.log("[Config] Failed to load config file: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Finds the first available configuration file in the predefined locations.
     */
    public Optional<File> findConfigFile() {
        return CONFIG_LOCATIONS.stream()
                .map(File::new)
                .filter(File::exists)
                .peek(file -> Logger.log("[Config] Found config file: " + file.getPath()))
                .findFirst();
    }

    /**
     * Retrieves a configuration value by its key.
     *
     * @param key  The key path to the value (e.g., "database.host").
     * @param type The expected type of the value.
     * @param <T>  The type parameter.
     * @return The configuration value, or {@code null} if not found or mismatched type.
     */
    public <T> T getValue(Class<T> type, String key) {
        Objects.requireNonNull(key, "Key cannot be null");

        String[] keys = key.split("\\.");
        Object value = configData;

        for (String k : keys) {
            if (value instanceof Map<?, ?> map) {
                value = map.get(k);
            } else {
                return null; // Invalid path
            }
        }

        if (type.isInstance(value)) {
            return type.cast(value);
        }

        return null; // Mismatched type or missing key
    }

    /**
     * Retrieves a configuration value as a String.
     */
    public Optional<String> getString(String key) {
        return Optional.ofNullable(getValue(String.class, key));
    }

    /**
     * Sets a configuration value and saves the config file.
     *
     * @param key   The key path to set (e.g., "server.name").
     * @param value The new value to set.
     */
    public void setValue(String key, Object value) {
        lock.lock();
        try {
            // Traverse and update the map for nested keys
            String[] keys = key.split("\\.");
            Map<String, Object> map = configData;

            for (int i = 0; i < keys.length - 1; i++) {
                map = (Map<String, Object>) map.computeIfAbsent(keys[i], k -> new HashMap<>());
            }
            map.put(keys[keys.length - 1], value);

            saveConfig();
        } finally {
            lock.unlock();
        }
    }

    private void saveConfig() {
        if (loadedConfigFile == null) {
            Logger.log("[Config] No configuration file to save.");
            return;
        }

        try (Writer writer = new FileWriter(loadedConfigFile)) {
            Yaml yaml = new Yaml();
            yaml.dump(configData, writer);

            Logger.log("[Config] Config file saved successfully.");
        } catch (IOException e) {
            Logger.log("[Config] Failed to save config file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}