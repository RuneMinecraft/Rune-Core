package com.dank1234.utils.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

public final class Menu {
    private final MenuHolder holder;
    private final Integer size;
    private final List<ItemStack> items;
    private final List<BiConsumer<MenuHolder, InventoryClickEvent>> actions;

    private ItemStack filler;

    private Menu(final MenuHolder holder, final int size) {
        this.holder = holder;
        this.size = size;
        this.items = new ArrayList<>(this.size());
        this.actions = new ArrayList<>(this.size());
    }

    public Integer size() {
        return this.size;
    }
    public MenuHolder holder() {
        return this.holder;
    }
    public List<ItemStack> items() {
        return this.items;
    }
    public List<BiConsumer<MenuHolder, InventoryClickEvent>> actions() {
        return this.actions;
    }



    public Menu add(final ItemStack item) {
        return this;
    }
    public Menu set(final int i, final ItemStack item) {
        return this;
    }
    public ItemStack get(final int i) {
        return this.items().get(i);
    }

}