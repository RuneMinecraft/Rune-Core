package com.dank1234.utils.data;

import com.dank1234.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

// LATEST TEST SHOWS ITS WORKING - DAN (please dont break it you fucks)
public final class Config {

    // im just going to put list here instead of in the function
    private static final List<String> CONFIG_LOCATIONS = List.of(
        "plugins/Rune-Core/config.yml",
        "config/Rune-Core/config.yml"
    );

    // IntelliJ told me to add this so dont blame me
    private static final class InstanceHolder {
        private static final Config instance = new Config();
    }

    // added reentrant lock for thread safety
    private static final ReentrantLock lock = new ReentrantLock();

    private Map<String, Object> configMap;
    private File loadedConfigFile;
    
    private Config() {}

    public static Config get() {
        return InstanceHolder.instance;
    }

    public void loadConfig() {

        loadedConfigFile = findConfigFile().orElseGet(() -> {
            Logger.logRaw("[BootStrap | Config] No config file found.");
            return null;
        });

        try (InputStream input = new FileInputStream(loadedConfigFile)) {
            Yaml yaml = new Yaml();
            configMap = yaml.load(input);
            Logger.logRaw("[BootStrap | Config] Config file loaded successfully");
        } catch (IOException e) {
            Logger.logRaw("[Bootstrap | Config] Failed to load config file: : " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public Optional<File> findConfigFile() {

        // added streams for file search better than spamming if statements DAN
        return CONFIG_LOCATIONS.stream()
            .map(File::new)
            .filter(File::exists)
            .peek(file -> Logger.logRaw("[Bootstrap | Config] Found config file: " + file.getPath())).toList().stream().findAny();
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable(getValue(key, String.class));
    }

    public <T> T getValue(String key, Class<T> type) {

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
            return;
        }

        try (Writer writer = new FileWriter(loadedConfigFile)) {
            Yaml yaml = new Yaml();
            yaml.dump(configMap, writer);
            Logger.logRaw("[Bootstrap | Config] Config file saved successfly.");
        } catch (IOException e) {
            Logger.logRaw("[Bootstrap | Config] Filed to save config file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
