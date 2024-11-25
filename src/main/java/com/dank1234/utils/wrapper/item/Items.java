package com.dank1234.utils.wrapper.item;

import org.bukkit.Material;

public enum Items {
    BACK_ARROW(Item.create(Material.PAPER, "&c&lBack Page")),
    NEXT_ARROW(Item.create(Material.STICK, "&a&lNext Page"));

    private final Item item;
    Items(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }
}
