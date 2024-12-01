package com.dank1234.plugin.box;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class BoxManager {
    public static final World w = Bukkit.getWorld("box");

    public static void generateAllMines() {
        List.of(Mines.values()).forEach((BoxManager::generate));
    }

    private static void generate(Mines mine) {

    }
}
