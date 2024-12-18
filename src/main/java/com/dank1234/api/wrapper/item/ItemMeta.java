package com.dank1234.api.wrapper.item;

import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nullable;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ItemMeta implements Serializable {
    private static final @Serial long serialVersionUID = 1L;

    private final Map<Enchantment, Integer> enchants;
    private final boolean unbreakable;
    private int customModelData;

    public ItemMeta() {
        this.enchants = new HashMap<>();
        this.customModelData = 0;
        this.unbreakable = false;
    }
    public ItemMeta(Map<Enchantment, Integer> enchants, int customModelData, boolean unbreakable) {
        this.enchants = enchants != null ? enchants : new HashMap<>();
        this.customModelData = customModelData;
        this.unbreakable = unbreakable;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return new HashMap<>(enchants);
    }
    public ItemMeta addEnchant(Enchantment enchantment, int level) {
        enchants.put(enchantment, level);
        return this;
    }
    public ItemMeta removeEnchant(Enchantment enchantment) {
        this.getEnchants().remove(enchantment);
        return this;
    }

    public int getCustomModelData() {
        return customModelData;
    }
    public ItemMeta setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void applyTo(org.bukkit.inventory.meta.ItemMeta bukkitMeta) {
        if (bukkitMeta != null) {
            enchants.forEach((enchant, level) -> bukkitMeta.addEnchant(enchant, level, true));
            bukkitMeta.setUnbreakable(unbreakable);
            bukkitMeta.setCustomModelData(customModelData);
        }
    }

    public static ItemMeta fromBukkitMeta(@Nullable org.bukkit.inventory.meta.ItemMeta bukkitMeta) {
        if (bukkitMeta == null) {
            return new ItemMeta();
        }

        Map<Enchantment, Integer> enchants = new HashMap<>(bukkitMeta.getEnchants());
        int customModelData = bukkitMeta.hasCustomModelData() ? bukkitMeta.getCustomModelData() : 0;
        boolean unbreakable = bukkitMeta.isUnbreakable();

        return new ItemMeta(enchants, customModelData, unbreakable);
    }

    /*
    private @Serial void writeObject(ObjectOutputStream out) {
        out.defaultWriteObject();
    }
    private @Serial void readObject(ObjectInputStream in) {
        in.defaultReadObject();
    }
    private void writeExternalData(ObjectOutputStream out) {
        out.writeObject(enchants);
        out.writeInt(customModelData);
        out.writeBoolean(unbreakable);
    }
    private @SuppressWarnings("unchecked") void readExternalData(ObjectInputStream in) {
        Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>) in.readObject();
        int amount = in.readInt();
        boolean unbreakable = in.readBoolean();
    }
    public static byte[] serializeItem(ItemMeta item) {
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(item);
                return byteArrayOutputStream.toByteArray();
            }
        }catch (Exception e) {
            throw new IllegalStateException("An error occured whilst serializing an ItemMeta.");
        }
    }
    public static ItemMeta deserializeItem(byte[] bytes) {
        try{
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                return (ItemMeta) objectInputStream.readObject();
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occured whilst deserializing an ItemMeta.");
        }
        return null;
    }

     */
}