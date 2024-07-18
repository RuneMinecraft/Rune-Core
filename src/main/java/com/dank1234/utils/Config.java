package com.dank1234.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Config {
    private static Config instance;
    private Map<String, Object> configMap;

    private Config() {
        loadConfig();
    }

    public static Config get() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private void loadConfig() {
        File configFile = find();
        if (configFile != null) {
            try (InputStream input = new FileInputStream(configFile)) {
                Yaml yaml = new Yaml();
                configMap = yaml.load(input);
            } catch (Exception e) {
                e.printStackTrace();
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
                return file;
            }
        }

        return null;
    }

    public String getValue(String key) {
        if (configMap != null && configMap.containsKey(key)) {
            return configMap.get(key).toString();
        }
        return null;
    }
}