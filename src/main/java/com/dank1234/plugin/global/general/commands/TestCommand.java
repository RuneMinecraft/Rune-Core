package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.command.CommandSender;

@Cmd(names = "test")
public class TestCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        BlockType blockType = BlockType.REGISTRY.get("stone");
        assert blockType != null;
        BlockState blockState = blockType.getDefaultState();

        BlockVector3 pos1 = BlockVector3.at(100, 100, 100);
        BlockVector3 pos2 = BlockVector3.at(-100, 100, -100);

        try (EditSession editSession = WorldEdit.getInstance()
                .getEditSessionFactory()
                .getEditSession(BukkitAdapter.adapt(player().getWorld()), -1)) {
            editSession.setBlocks(
                    new CuboidRegion(pos1, pos2).getFaces(),
                    blockState
            );
        }
    }
}
