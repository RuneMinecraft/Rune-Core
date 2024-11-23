package com.dank1234.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.model.data.DataType;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.Flag;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;

public final class RankUtils {
    private static final LuckPerms luckperms = LuckPermsProvider.get();

    public static Group getGroup(com.dank1234.utils.wrapper.player.User player) {
        User user = luckperms.getUserManager().getUser(player.uuid());
        if (user == null) {
            Logger.log("A null user has been passed through the RankUtils.getGroup(Player) method. \nPlease check through the code to find any errors.");
            return Consts.MEMBER_GROUP;
        }
        return luckperms.getGroupManager().getGroup(user.getPrimaryGroup());
    }
    public static String getPrefix(com.dank1234.utils.wrapper.player.User player) {
        User user = luckperms.getUserManager().getUser(player.uuid());
        if (user == null) {
            Logger.log("A null user has been passed through the RankUtils.getPrefix(Player) method. \nPlease check through the code to find any errors.");
            return Consts.MEMBER_GROUP.getCachedData().getMetaData().getPrefix();
        }
        return user.getCachedData().getMetaData().getPrefix();
    }
    public static void removeStaffTrack(com.dank1234.utils.wrapper.player.User user) {
        LuckPerms lp = LuckPermsProvider.get();
        lp.getGroupManager().getLoadedGroups().forEach(group -> {
            User userObject = lp.getUserManager().getUser(user.uuid());
            if (userObject != null && userObject.getInheritedGroups(QueryOptions.defaultContextualOptions()).contains(group)
                    && group.getName().startsWith("staff.")) {
                userObject.data().remove(Node.builder("group." + group.getName()).build());
                lp.getUserManager().saveUser(userObject);
            }
        });
    }
}
