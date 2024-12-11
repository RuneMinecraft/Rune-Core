package com.dank1234.utils.wrapper.inventory;

import com.dank1234.utils.Utils;
import com.dank1234.utils.wrapper.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@NotNull
public class Menu implements Utils {
    private final @NotNull List<User> holders = new ArrayList<>();
    private final @Nullable String title;
    private final int size;
    private final @NotNull List<Item> contents;
    private final Inventory inventory;
    private final Map<Integer, Consumer<User>> actions = new HashMap<>();

    private Menu(@Nullable String title, int size) {
        checkExpression(size % 9 != 0 || size <= 0 || size > 54, "Index is out of bounds for menu: "+this);

        this.title = title;
        this.size = size;
        this.contents = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            contents.add(null);
        }
        this.inventory = Bukkit.createInventory(null, size, title != null ? title : "Menu");
    }

    public static Menu create(@Nullable String title, int size) {
        return new Menu(title, size);
    }

    public @NotNull List<User> holders() {
        return this.holders;
    }
    public @Nullable String title() {
        return this.title;
    }
    public int size() {
        return this.size;
    }
    public @NotNull List<Item> contents() {
        return this.contents;
    }
    public Inventory inventory() {
        return this.inventory;
    }
    public Map<Integer, Consumer<User>> actions() {
        return this.actions;
    }

    public Menu addHolder(@NotNull User user) {
        if (!holders.contains(user)) {
            holders.add(user);
        }
        return this;
    }
    public Menu removeHolder(@NotNull User user) {
        holders.remove(user);
        return this;
    }

    public Menu setItem(int slot, @Nullable Item item) {
        if (slot < 0 || slot >= size) {
            throw new IndexOutOfBoundsException("Slot index out of bounds.");
        }
        contents.set(slot, item);
        inventory.setItem(slot, item != null ? item.toBukkit() : null);
        return this;
    }
    public boolean addItem(@NotNull Item item) {
        for (int i = 0; i < size; i++) {
            if (contents.get(i) == null) {
                setItem(i, item);
                return true;
            }
        }
        return false;
    }
    public @Nullable Item getItem(int slot) {
        checkExpression(slot < 0 || slot >= size, "Slot out of bounds in an access point!");
        return contents.get(slot);
    }

    public Menu setAction(int slot, @NotNull Consumer<User> action) {
        if (slot < 0 || slot >= size) {
            throw new IndexOutOfBoundsException("Slot index out of bounds.");
        }
        actions.put(slot, action);
        return this;
    }
    public Menu triggerAction(int slot, @NotNull User player) {
        if (actions.containsKey(slot)) {
            actions.get(slot).accept(player);
        }
        return this;
    }

    public Menu open(@NotNull User user) {
        this.addHolder(user);
        user.openMenu(this);
        return this;
    }
    public Menu close(@NotNull User user) {
        this.removeHolder(user);
        user.getPlayer().closeInventory();
        return this;
    }

    public Menu fill(int[] slots, Material material) {
        for (int i : slots) {
            this.setItem(i, Item.create(material));
        }
        return this;
    }
    public Menu fill(Material material) {
        for (int i = 0; i < 54; i++) {
            if (inventory().getItem(i) == null) return this;
            if (inventory().getItem(i).getType() == Material.AIR) return this;

            this.setItem(i, Item.create(material));
        }
        return this;
    }
}