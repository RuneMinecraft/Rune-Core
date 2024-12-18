package com.dank1234.plugin.box;

import com.dank1234.api.wrapper.item.Item;
import org.bukkit.Location;
import org.bukkit.Material;

import static com.dank1234.plugin.box.BoxManager.*;
import static org.bukkit.Material.*;

public enum Mines {
    WOOD(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(OAK_LOG), Item.create(STRIPPED_OAK_LOG)),
    STONE(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(COBBLESTONE), Item.create(Material.STONE)),
    COAL(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(COAL_ORE), Item.create(COAL_BLOCK)),
    IRON(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(IRON_ORE), Item.create(IRON_BLOCK)),
    GOLD(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(GOLD_ORE), Item.create(GOLD_BLOCK)),
    DIAMOND(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(DIAMOND_ORE), Item.create(DIAMOND_BLOCK)),
    EMERALD(new Location(w, 1, 1 ,1), new Location(w, 1, 1 ,1), Item.create(EMERALD_ORE), Item.create(EMERALD_BLOCK));

    private final Item i1;
    private final Item i2;
    private final Location l1;
    private final Location l2;

    Mines(Location l1, Location l2, Item i1, Item i2) {
        this.i1=i1;
        this.i2=i2;
        this.l1=l1;
        this.l2=l2;
    }

    public Item i1() {
        return this.i1;
    }
    public Item i2() {
        return this.i2;
    }
    public Location loc1() {
        return this.l1;
    }
    public Location loc2() {
        return this.l2;
    }
}
