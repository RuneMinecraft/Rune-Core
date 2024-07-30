package com.dank1234.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

public final class RankUtils {
    private static final LuckPerms luckperms = LuckPermsProvider.get();

    public static Group getGroup(Player player) {
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            Logger.log("A null user has been passed through the RankUtils.getGroup(Player) method. \nPlease check through the code to find any errors.");
            return Consts.MEMBER_GROUP;
        }
        return luckperms.getGroupManager().getGroup(user.getPrimaryGroup());
    }
    public static String getPrefix(Player player) {
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            Logger.log("A null user has been passed through the RankUtils.getGroup(Player) method. \nPlease check through the code to find any errors.");
            return Consts.MEMBER_GROUP.getCachedData().getMetaData().getPrefix();
        }
        return user.getCachedData().getMetaData().getPrefix();
    }
}
