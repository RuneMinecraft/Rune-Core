package com.dank1234.plugin.box.events;

import com.dank1234.plugin.box.Mines;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class MineRunnable {
    public static void start() {
        for (Mines mine : Mines.values()) {
            Location loc1 = mine.loc1();
            Location loc2 = mine.loc2();

            BlockVector3 pos1 = BlockVector3.at(loc1.getX(), loc1.getY(), loc1.getZ());
            BlockVector3 pos2 = BlockVector3.at(loc2.getX(), loc2.getY(), loc2.getZ());

            BlockState blockState = null;//new BlockState(BlockType.REGISTRY.get(mine.i1().material().name().toLowerCase())., );

            try (EditSession editSession = WorldEdit.getInstance()
                    .getEditSessionFactory()
                    .getEditSession(BukkitAdapter.adapt(Bukkit.getWorld("void")), -1)) {
                editSession.setBlocks(
                        new CuboidRegion(pos1, pos2).getFaces(),
                        blockState
                );
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }
    }
}