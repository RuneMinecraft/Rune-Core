package com.dank1234.utils.wrapper.item;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ItemUtils {
    public static @NotNull String getMaterialName(Material material) {
        String[] words = material.name().toLowerCase().split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return "&r"+result.toString().trim();
    }
}
