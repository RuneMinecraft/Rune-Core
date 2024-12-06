package com.dank1234.utils.wrapper.item;

import com.dank1234.utils.Utils;
import com.dank1234.utils.wrapper.inventory.Menu;

import java.util.*;

public class ItemContainer implements Utils {
    private final Map<Integer, Item> items;

    private ItemContainer(Map<Integer, Item> items) {
        this.items = items;
    }

    public static ItemContainer create() {
        return new ItemContainer(new LinkedHashMap<>());
    }
    private static ItemContainer create(Map<Integer, Item> items) {
        return new ItemContainer(items);
    }

    public Map<Integer, Item> items() {
        return this.items;
    }
    public List<Item> getAllItems() {
        return Arrays.asList(this.items().values().toArray(Item[]::new));
    }

    public ItemContainer add(Item ... items) {
        Arrays.stream(items).toList().forEach((item) -> {
            this.set(items().size(), item);
        });
        return this;
    }
    public ItemContainer set(int key, Item value) {
        this.items().put(key, value);
        return this;
    }

    public Menu toMenu() {
        Menu menu = Menu.create("Test", 54);
        this.items().forEach((key, value) -> {
            menu.setItem(key, value);
        });
        return menu;
    }
}
