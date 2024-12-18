package com.dank1234.api.wrapper.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public enum Items {
    BACK_ARROW(Item.create(Material.PAPER, "&c&lBack Page", new ItemMeta().setCustomModelData(1))),
    NEXT_ARROW(Item.create(Material.STICK, "&a&lNext Page", new ItemMeta().setCustomModelData(2))),

    EPIC_SWORD(Item.create(Material.DIAMOND_SWORD, "&c&lEPIC SWORD", new ItemMeta(Map.of(Enchantment.SWIFT_SNEAK, 2), 0, true)));

    private final Item item;
    Items(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }
}
