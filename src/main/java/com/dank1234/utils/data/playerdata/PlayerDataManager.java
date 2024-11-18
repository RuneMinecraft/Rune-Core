package com.dank1234.utils.data.playerdata;

import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class PlayerDataManager extends DataManager<Player> implements Persistable {
    private final Player player;
    private List<Data<String, String>> data;

    private PlayerDataManager(Player player) {
        super(player);
        this.player = player;
        loadData();
    }

    public static PlayerDataManager get(Player player) {
        return new PlayerDataManager(player);
    }

    @Override
    public void setData(String key, Object value) {
        String valueStr = value.toString();
        Data<String, String> existingData = getData(key);
        if (existingData != null) {
            existingData.value();
        } else {
            data.add(Data.of(key, valueStr));
        }
    }

    //@Override
    public List<Data<String, String>> getData() {
        return new ArrayList<>(data);
    }

    public Data<String, String> getData(String key) {
        return data.stream()
            .filter(datum -> datum.key().equals(key))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Data<String, String>> getAllData() {
        return List.of();
    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }

    @Override
    public void saveData() {
        File file = new File(this.getFileName());
        ensureFileExists(file);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        File file = new File(this.getFileName());
        ensureFileExists(file);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            data = (List<Data<String, String>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            data = new ArrayList<>();
        }
    }

    private String getFileName() {
        return "data/pdc_" + player.getUniqueId() + ".dat";
    }

    private void ensureFileExists(File file) {
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}