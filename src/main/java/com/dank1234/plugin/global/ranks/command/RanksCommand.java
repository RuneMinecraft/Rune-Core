package com.dank1234.plugin.global.ranks.command;

import com.dank1234.plugin.global.ranks.command.subcommands.UserCommand;
import com.dank1234.plugin.global.ranks.command.subcommands.GroupCommand;
import com.dank1234.plugin.global.ranks.command.subcommands.TrackCommand;
import com.dank1234.plugin.global.ranks.command.subcommands.PromoteCommand;
import com.dank1234.plugin.global.ranks.command.subcommands.DemoteCommand;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import org.bukkit.command.CommandSender;

@Cmd(names = "ranks")
public class RanksCommand extends ICommand {
    /*
     * /ranks user [USER] group set [RANK]
     * /ranks user [USER] group add [RANK]
     * /ranks user [USER] group remove [RANK]
     *
     *
     * /ranks user [USER] track set [TRACK]
     * /ranks user [USER] track remove [TRACK]
     * /ranks promote [USER] [TRACK]
     * /ranks demote [USER] [TRACK]
     *
     * /ranks user [USER] permissions add [PERMISSION]
     * /ranks user [USER] permissions remove [PERMISSION]
     *
     * /ranks group [GROUP] name set [NEW NAME]
     * /ranks group [GROUP] prefix set [NEW PREFIX]
     * /ranks group [GROUP] suffix set [NEW SUFFIX]
     *
     * /ranks group [GROUP] inherited-groups add [RANK]
     * /ranks group [GROUP] inherited-groups remove [RANK]
     *
     * /ranks group [GROUP] permissions add [PERMISSION]
     * /ranks group [GROUP] permissions remove [PERMISSIONS]
     *
     * /ranks track [TRACK] group add [RANK]
     * /ranks track [TRACK] group remove [RANK]
    */

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Invalid command usage. Type '/ranks help' for help.");
            return;
        }

        try {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "user" -> UserCommand.execute(sender, args);
                case "group" -> GroupCommand.execute(sender, args);
                case "track" -> TrackCommand.execute(sender, args);
                case "promote" -> PromoteCommand.execute(sender, args);
                case "demote" -> DemoteCommand.execute(sender, args);
                default -> sender.sendMessage("Unknown subcommand. Type '/ranks help' for help.");
            }
        } catch (Exception e) {
            sender.sendMessage("An error occurred while processing the command: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
