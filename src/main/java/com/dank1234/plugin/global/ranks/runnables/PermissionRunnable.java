package com.dank1234.plugin.global.ranks.runnables;

import com.dank1234.plugin.Codex;
import com.dank1234.plugin.Main;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.Bukkit;

public class PermissionRunnable {
    public static void start() {
        Bukkit.getScheduler().runTaskTimer(Main.get(), PermissionRunnable::updatePermissions, 0L, 10L);
    }

    private static void updatePermissions() {
        for (User user : Codex.getOnlineUsers()) {
            System.out.println(user.getUsername());
        }
    }
}
