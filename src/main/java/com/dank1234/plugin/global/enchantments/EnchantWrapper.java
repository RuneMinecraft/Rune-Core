package com.dank1234.plugin.global.enchantments;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public abstract class EnchantWrapper extends Enchantment {
    public abstract String name();
    public abstract int startLevel();
    public abstract int maxLevel();
    public abstract List<Material>[] getTargetTypes();
    public abstract List<Enchantment> conflicts();
    public abstract boolean treasure();
    public abstract boolean cursed();

    public @Override @NotNull String getName() {
        return this.name();
    }
    public @Override int getStartLevel() {
        return this.startLevel();
    }
    public @Override int getMaxLevel() {
        return this.maxLevel();
    }
    public @Override boolean isTreasure() {
        return this.treasure();
    }
    public @Override boolean isCursed() {
        return this.cursed();
    }
    public @Override boolean conflictsWith(@NotNull Enchantment enchantment) {
        return this.conflicts().contains(enchantment);
    }
    public @Override boolean canEnchantItem(@NotNull ItemStack itemStack) {
        if (getTargetTypes() != null) {
            Material type = itemStack.getType();
            for (List<Material> materials : getTargetTypes()) {
                if (materials.contains(type)) return true;
            }
        }
        return false;
    }
    public @Override @NotNull Component displayName(int i) {
        return Component.text(this.name());
    }
    public @Override @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of();
    }

    public @Override int getWeight() {
        return 10;
    }
    public @Override boolean isTradeable() {
        return false;
    }
    public @Override boolean isDiscoverable() {
        return false;
    }
    public @Override int getMinModifiedCost(int i) {
        return -1;
    }
    public @Override int getMaxModifiedCost(int i) {
        return -1;
    }
    public @Override int getAnvilCost() {
        return -1;
    }
}
