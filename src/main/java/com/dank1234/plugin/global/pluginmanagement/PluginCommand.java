package com.dank1234.plugin.global.pluginmanagement;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@Cmd(
        names= {"plugin", "pl"},
        perms= {"admin"}
)
@SuppressWarnings("removal")
public class PluginCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // TODO: Message for all plugins with clickable info.
            return;
        }

        if (super.checkArgument(0, "enable")) {
            if (args.length != 2) {
                Message.create(sender(), Messages.ARGUMENTS+" &cUsage: /plugin enable <name>").send(false);
                return;
            }

            String pluginName = args[1];
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);

            if (plugin != null) {
                if (plugin.isEnabled()) {
                    return;
                }
                // Bootstrap.pluginLoader().enablePlugin(plugin);
            }
        }
        else if (super.checkArgument(0, "disable")) {
            if (args.length != 2) {
                Message.create(sender(), Messages.ARGUMENTS+" &cUsage: /plugin disable <name>").send(false);
                return;
            }

            String pluginName = args[1];
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);

            if (plugin != null) {
                if (!plugin.isEnabled()) {
                    return;
                }
                // Bootstrap.pluginLoader().disablePlugin(plugin);
            }
        }
    }
}
