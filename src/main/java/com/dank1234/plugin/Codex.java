package com.dank1234.plugin;

import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.*;

public final class Codex {
    private static final Map<UUID, User> cachedUsers = new HashMap<>();

    public static void addUser(User user) {
        cachedUsers.put(user.getUuid(), user);
    }
    public static void removeUser(UUID uniqueId) {
        cachedUsers.remove(uniqueId);
    }
    public static User getUser(UUID uniqueId) {
        return cachedUsers.get(uniqueId);
    }

    public static Collection<User> getCachedUsers() {
        return Collections.unmodifiableCollection(cachedUsers.values());
    }
    public static Collection<UUID> getCachedUUIDs() {
        return Collections.unmodifiableCollection(cachedUsers.keySet());
    }

    public static Collection<User> getOnlineUsers() {
        List<User> users = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            users.add(Codex.getUser(player.getUniqueId()));
        }
        return users;
    }
}