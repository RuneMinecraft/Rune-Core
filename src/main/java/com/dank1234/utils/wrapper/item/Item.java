package com.dank1234.utils.wrapper.item;

import com.dank1234.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;

@NotNull
public class Item implements Utils, Serializable {
    private static final @Serial long serialVersionUID = 1L;

    private final @NotNull Material material;
    private @NotNull String displayName;
    private int amount;
    private @Nullable List<String> lore;
    private @NotNull ItemMeta itemMeta;

    private Item(@NotNull final Material material, @Nullable String displayName, int amount, @Nullable List<String> lore, @NotNull ItemMeta itemMeta) {
        this.material = material;

        if (displayName == null) displayName = ItemUtils.getMaterialName(material);
        this.displayName = displayName;

        if (amount == 0) amount = 1;
        this.amount = amount;

        this.lore = lore;
        this.itemMeta = itemMeta;
    }

    /*
     * FACTORY METHODS
     */

    public static Item of(@NotNull ItemStack itemStack) {
        Material material = itemStack.getType();

        String displayName = null;
        org.bukkit.inventory.meta.ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            displayName = "&r"+meta.getDisplayName();
        }

        int amount = itemStack.getAmount();

        List<String> lore = null;
        if (meta != null && meta.hasLore()) {
            lore = meta.getLore();
        }

        return new Item(material, displayName, amount, lore, ItemMeta.fromBukkitMeta(itemStack.getItemMeta()));
    }

    public static Item create(@NotNull final Material material) {
        return new Item(material, null, 1, null, new ItemMeta());
    }
    public static Item create(@NotNull final Material material, int amount) {
        return new Item(material, null, amount, null, new ItemMeta());
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName) {
        return new Item(material, displayName, 1, null, new ItemMeta());
    }
    public static Item create(@NotNull final Material material, @Nullable String... lore) {
        return new Item(material, null, 1, List.of(lore), new ItemMeta());
    }
    public static Item create(@NotNull final Material material, @NotNull ItemMeta itemMeta) {
        return new Item(material, null, 1, null, itemMeta);
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, int amount) {
        return new Item(material, displayName, amount, null, new ItemMeta());
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, @NotNull ItemMeta itemMeta) {
        return new Item(material, displayName, 1, null, itemMeta);
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, @NotNull String ... lore) {
        return new Item(material, displayName, 1, List.of(lore), new ItemMeta());
    }
    public static Item create(@NotNull final Material material, int amount, String... lore) {
        return new Item(material, null, amount, List.of(lore), new ItemMeta());
    }
    public static Item create(@NotNull final Material material, int amount, ItemMeta meta) {
        return new Item(material, null, amount, List.of(), meta);
    }
    public static Item create(@NotNull final Material material, ItemMeta meta, @NotNull String ... lore) {
        return new Item(material, null, 1, List.of(lore), meta);
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, int amount, @NotNull String... lore) {
        return new Item(material, displayName, amount, List.of(lore), new ItemMeta());
    }
    public static Item create(@NotNull final Material material, int amount, @NotNull ItemMeta meta, @NotNull String... lore) {
        return new Item(material, null, amount, List.of(lore), meta);
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, @NotNull ItemMeta meta, @NotNull String... lore) {
        return new Item(material, displayName, 1, List.of(lore), meta);
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, int amount, @NotNull ItemMeta meta) {
        return new Item(material, displayName, amount, List.of(), meta);
    }
    public static Item create(@NotNull final Material material, @Nullable String displayName, int amount, @NotNull ItemMeta meta, @NotNull String...lore) {
        return new Item(material, displayName, amount, List.of(lore), meta);
    }

    /*
     * GETTERS AND SETTERS
     *
     * please keep all the getters and setters to the same format so that I don't have
     * to search through the classes to find the formats of others
     */

    public @NotNull Material material() {
        return this.material;
    }
    public @Nullable String displayName() {
        return this.displayName;
    }
    public Item setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
        return this;
    }
    public int amount() {
        return this.amount;
    }
    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    public @Nullable List<String> lore() {
        return this.lore;
    }
    public Item setLore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
    }
    public @NotNull ItemMeta itemMeta() {
        return this.itemMeta;
    }
    public Item setItemMeta(@NotNull ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemStack toBukkit() {
        ItemStack itemStack = new ItemStack(this.material(), this.amount());
        org.bukkit.inventory.meta.ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
            if (itemMeta == null) {
                Bukkit.getLogger().warning("Failed to create ItemMeta for material: " + this.material());
                return itemStack;
            }
        }
        itemMeta.setDisplayName(Colour(this.displayName()));

        if (this.lore() != null) this.lore().forEach(this::Colour);
        itemMeta.setLore((this.lore() == null ? List.of() : this.lore()));

        for (Enchantment enchantment : this.itemMeta().getEnchants().keySet()) {
            itemMeta.addEnchant(enchantment, this.itemMeta().getEnchants().get(enchantment), true);
        }
        itemMeta.setCustomModelData(this.itemMeta().getCustomModelData());
        itemMeta.setUnbreakable(this.itemMeta().isUnbreakable());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*
     * SERIALIZATION CODE
     *
     * If any of you fucks touch this then it will most likely break the auctions database
     * do don't because you will be the one fixing the fuck-up.
     */

    private @Serial void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serialize all non-transient fields
    }
    private @Serial void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
    private void writeExternalData(ObjectOutputStream out) throws IOException {
        out.writeUTF(material.name());
        out.writeUTF(displayName);
        out.writeInt(amount);
        out.writeObject(lore);
    }
    private void readExternalData(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Material material = Material.valueOf(in.readUTF());
        String displayName = in.readUTF();
        int amount = in.readInt();
        List<String> lore = (List<String>) in.readObject();
    }
    public static byte[] serializeItem(Item item) {
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(item);
                return byteArrayOutputStream.toByteArray();
            }
        }catch (Exception e) {
            throw new IllegalStateException("An error occured whilst serializing an item.");
        }
    }
    public static Item deserializeItem(byte[] bytes) {
        try{
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                return (Item) objectInputStream.readObject();
            }
        }catch (Exception e) {
            throw new IllegalStateException("An error occured whilst deserializing an item.");
        }
    }
}
