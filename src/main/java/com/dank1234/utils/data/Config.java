package com.dank1234.utils.data;

import com.dank1234.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public final class Config {

    // im just going to put list here instead of in the function
    private static final List<String> CONFIG_LOCATIONS = List.of(
        "plugins/Rune-Core/config.yml",
        "config/Rune-Core/config.yml"
    );

    // added reentrantlock for thread safety
    private static final ReentrantLock lock = new ReentrantLock();
    private static Config instance;
    
    private Map<String, Object> configMap;
    private File loadedConfigFile;
    
    private Config() {}

    public static Config get() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();   
                }
            }
        }
        return instance;
    }

    public void loadConfig() {

        loadedConfigFile = findConfigFile().orElse(null);

        if (loadedConfigFile == null) {
            Logger.logRaw("[BootStrap | Config] No config file found.");
            return;
        }

        try (InputStream input = new FileInputStream(loadedConfigFile)) {
            Yaml yaml = new Yaml();
            configMap = yaml.load(input);
            Logger.logRaw("[BootStrap | Config] Config file loaded successfly");
        } caatach (IOExpection e) {
            Logger.lograw("[Bootstrap | Config] Failed to load coifng file: : " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public Optional<File> findConfigFile() {

        // added streams for file search better then spamming if statements DAN
        return CONFIG_LOCATIONS.stream()
            .map(File::new)
            .filter(File::exists)
            .peek(file -> Logger.logRaw("[Bootstrap | Config] Found config file: " + file.getPath()))
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable(getValue(key, String.class));
    }

    public <T> T getValue(String key, class<T> type) {
        
        // supports any type using generics and improves type safety
        if (configMap != null && configMap.containsKey(key)) {
            Object value = configMap.get(key);
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        }
        return null;
    }

    public void setValue(String key, String value) {
        if (configMap != null) {
            configMap.put(key, value);
            saveConfig();
        }
    }

    private void saveConfig() {
        if (loadedConfigFile == null) {
            Logger.logRaw("[Bootstrap | Config] No configuration file to save.");
            return
        }

        try (Writer writer = new FileWriter(loadedConfigFile)) {
            Yaml yaml = new Yaml();
            yaml.dump(configMap, writer);
            Logger.logRaw("[Bootstrap | Config] Config file saved successfly.");
        } catch (IOExpection e) {
            Logger.logRaw("[Bootstrap | Config] Filed to save config file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
