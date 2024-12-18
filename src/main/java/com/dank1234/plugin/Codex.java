package com.dank1234.plugin;

import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.*;

public final class Codex {
    private static final Map<UUID, User> cachedUsers = new HashMap<>();
    private static final Map<String, ICommand> registeredCommands = new HashMap<>();
    private static final List<Listener> registeredListeners = new ArrayList<>();

    /*
     * User Caching
     * Saves memory as significantly fewer calls are made to the database.
     */

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
        for (org.bukkit.entity.Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            users.add(Codex.getUser(player.getUniqueId()));
        }
        return users;
    }

    /*
     * Commands Storing
     * Allows for commands to be hot reloaded and disabled with our own command register
     */

    public static void addCommand(String command, ICommand clazz) {
        registeredCommands.put(command, clazz);
    }
    public static void removeCommand(String command) {
        registeredCommands.remove(command);
    }
    public static ICommand getCommand(String command) {
        return registeredCommands.get(command);
    }

    public static Collection<String> getAllCommandNames() {
        return registeredCommands.keySet();
    }
    public static Collection<ICommand> getAllCommandClasses() {
        return registeredCommands.values();
    }

    /*
     * Event Storing
     * Allows for events to be hot reloaded with our own event register
     */

    public static void addEvent(Listener clazz) {
        registeredListeners.add(clazz);
    }
    public static void removeEvent(Listener clazz) {
        registeredListeners.remove(clazz);
    }

    public static Collection<Listener> getAllEvents() {
        return registeredListeners;
    }
}