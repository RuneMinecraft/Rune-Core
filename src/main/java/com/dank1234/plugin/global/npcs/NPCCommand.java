package com.dank1234.plugin.global.npcs;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import org.bukkit.command.CommandSender;

@Cmd(names="npc")
public class NPCCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        NPC npc = NPC.create(args(0), null, player().getLocation(), (player) -> {});
    }
}
