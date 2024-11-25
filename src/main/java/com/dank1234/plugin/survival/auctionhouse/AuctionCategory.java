package com.dank1234.plugin.survival.auctionhouse;

import com.dank1234.utils.wrapper.item.Item;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum AuctionCategory {
    BLOCKS(Item.create(Material.BRICKS, "&#eb6238&lBlocks"), Material.AIR),
    MINERALS(Item.create(Material.LAPIS_LAZULI, "&#2773e6&lMinerals"), Material.AIR),
    COMBAT_AND_TOOLS(Item.create(Material.GOLDEN_SWORD, "&#bf9900&lCombat & Tools"), Material.AIR),
    FARMING(Item.create(Material.WHEAT, "&#92bf00&lFarming"), Material.AIR),
    MOB_DROPS(Item.create(Material.ROTTEN_FLESH, "&#9e3324&lMob Drops"), Material.AIR),
    FOOD(Item.create(Material.COOKED_BEEF, "&#e66d2c&lFood"), Material.AIR),
    REDSTONE(Item.create(Material.REDSTONE, "&#e60000&lRedstone"), Material.AIR),
    VOUCHERS(Item.create(Material.PAPER, "&#00a5e6&lVouchers"), Material.AIR),
    MISC(Item.create(Material.MUSIC_DISC_CAT, "&#cc18a2&lMisc"), Material.AIR);

    private final Item item;
    private final List<Item> items = new ArrayList<>();;
    AuctionCategory(Item item, Item ... items) {
        this.item = item;
        this.items.addAll(List.of(items));
    }
    AuctionCategory(Item item, Material ... items) {
        this.item = item;
        List.of(items).forEach((material) -> this.items().add(Item.create(material)));
    }

    public Item item() {
        return this.item;
    }
    public List<Item> items() {
        return this.items;
    }
}
