package com.dank1234.plugin.global.general.commands;

import com.dank1234.plugin.Main;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.Config;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
@Cmd(
        server = ServerType.GLOBAL,
        names = "rune",
        perms = "admin",
        disabled = false
)
public class RuneCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Config config = Config.get();
            // config.find("plugins/Rune-Core/credit.yml");

            List<String> developerList = (List<String>) config.getObjectValue("developers");
            if (developerList == null) {
                developerList = new ArrayList<>(Collections.singleton("dank1234"));
            }

            Message.create(super.sender(),
                    "&aThis plugin was coded by the RuneMC development team for the sole purpose of this server.",
                    "&aDevelopers: &f"+String.join("&a, &f", developerList),
                    "&aVersion: &f"+Main.get().version().toString(),
                    "&aSupport Email: &fsupport@runemc.net",
                    "&aOnline Store: &fbuy.runemc.net"
            ).send(false);
            return;
        }

        if (super.checkArgument(0, "reload")) {
            if (super.checkArgument(1, "commands")) {
                Message.create(super.sender(), "&eReloading commands...").send(true);
                try {
                    Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);
                    SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
                    commandMap.clearCommands();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                Main.get().register().autoRegisterCommands();
                Message.create(super.sender(), "&aReloaded commands.").send(true);
            }
            else if (super.checkArgument(1, "events")) {
                Message.create(super.sender(), "&eReloading events...").send(true);
                HandlerList.unregisterAll();
                Main.get().register().autoRegisterListeners();
                Message.create(super.sender(), "&aReloaded events.").send(true);
            }
        }
    }
}
