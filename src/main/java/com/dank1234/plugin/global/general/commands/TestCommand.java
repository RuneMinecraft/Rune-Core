package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.playerdata.PlayerDataManager;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd (names = "test")
public class TestCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlayerDataManager pdm = rPlayer().playerDataManager();
        pdm.loadData();
        pdm.saveData();

        Message.create(player(), pdm.getData("owner").toString()).send();
    }
}
