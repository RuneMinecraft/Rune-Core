package com.dank1234.plugin.global.npcs;

import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="npc")
public class NPCCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        try {
            //String[] skinData = SkinFetcher.fetchSkinData(args(0));
            //String skinValue = skinData[0];
            //String skinSignature = skinData[1];

            //NPCBuilder npc = new NPCBuilder(args(0));//, skinValue, skinSignature);
            //npc.setLocation(player().getLocation());
            //npc.spawn(player());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
