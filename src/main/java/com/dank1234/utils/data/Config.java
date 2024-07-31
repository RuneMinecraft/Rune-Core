package com.dank1234.utils.data;

import com.dank1234.utils.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Config {
    private static Config instance;
    private Map<String, Object> configMap;

    private Config() {}

    public static Config get() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void loadConfig() {
        File configFile = find();
        if (configFile != null) {
            try (InputStream input = new FileInputStream(configFile)) {
                Yaml yaml = new Yaml();
                configMap = yaml.load(input);
                Logger.logRaw("[Boostrap | Config] Config file loaded successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logRaw("[Boostrap | Config] Failed to load config file: " + e.getMessage());
            }
        }
    }

    public File find() {
        List<String> possibleLocations = Arrays.asList(
                "plugins/Rune-Core/config.yml",
                "config/Rune-Core/config.yml"
        );

        for (String location : possibleLocations) {
            File file = new File(location);
            if (file.exists()) {
                Logger.logRaw("[Boostrap | Config] Found config file.");
                return file;
            }
            Logger.logRaw("[Boostrap | Config] No config file found.");
        }
        return null;
    }

    public String getValue(String key) {
        if (configMap != null && configMap.containsKey(key)) {
            return configMap.get(key).toString();
        }
        return null;
    }

    public Object getObjectValue(String key) {
        if (configMap != null && configMap.containsKey(key)) {
            return configMap.get(key);
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
        File configFile = find();
        if (configFile != null) {
            try (Writer writer = new FileWriter(configFile)) {
                Yaml yaml = new Yaml();
                yaml.dump(configMap, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}