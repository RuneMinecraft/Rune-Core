package com.dank1234.plugin.global.ranks.runnables;

import com.dank1234.plugin.Codex;
import com.dank1234.plugin.Main;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionRunnable {
    public static void start() {
        Bukkit.getScheduler().runTaskTimer(Main.get(), (task) -> Codex.getOnlineUsers().forEach(PermissionRunnable::updatePermissions), 0L, 20L);
    }

    private static void updatePermissions(User user) {

        if (user.isOnline()) {
            Player player = user.getPlayer();

            List<String> permissions = new ArrayList<>(user.getPermissions());
            user.getGroups().forEach(group -> permissions.addAll(group.getPermissions()));

            Set<String> uniquePermissions = new HashSet<>(permissions);

            PermissionAttachment attachment = player.addAttachment(Main.get());

            uniquePermissions.forEach(permission -> {
                attachment.setPermission(permission, true);
            });

            player.getEffectivePermissions().forEach(info -> {
                if (uniquePermissions.contains(info.getPermission()) || !info.getValue()) {return;}
                attachment.setPermission(info.getPermission(), false);
            });
        }
    }
}
