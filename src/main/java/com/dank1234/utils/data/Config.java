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

    public File find(String ... locations) {
        try {
            for (String location : locations) {
                File file = new File(location);
                if (file.exists()) {
                    boolean created = file.createNewFile();
                    Logger.log("Could not find file at " + file.getCanonicalPath() + ". File created: " + created);
                }
                return file;
            }

            return null;
        }catch(IOException ignored) {
            return null;
        }
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