package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.inventory.Menu;
import com.dank1234.utils.wrapper.item.Item;
import com.dank1234.utils.wrapper.item.ItemContainer;
import com.dank1234.utils.wrapper.item.ItemMeta;
import com.dank1234.utils.wrapper.player.User;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

@Cmd(names = "test")
public class TestCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(Enchantment.CHANNELING, 2);
        ItemContainer menu = ItemContainer.create();
        menu.set(8, Item.create(Material.DIAMOND_SWORD, "Help me nigga", new ItemMeta(enchants, 0, true)));
        User.of(sender.getName()).openMenu(menu.toMenu());
    }
}
