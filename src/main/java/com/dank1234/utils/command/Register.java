package com.dank1234.utils.command;

import com.dank1234.plugin.Main;
import com.dank1234.utils.Locale;
import com.dank1234.utils.Logger;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.*;

public final class Register {
    private static Register instance;

    private Register() {
    }
    public static Register get() {
        if (instance == null) {
            instance = new Register();
        }
        return instance;
    }

    private final Map<String, ICommand> commandHandlers = new HashMap<>();
    private final List<Listener> registeredListeners = new ArrayList<>();

    public void autoRegisterCommands() {
        ServerType currentServer = ServerType.HUB; // ServerType.valueOf(Main.get().config().getValue(String.class, "server.type"));

        Reflections reflections = new Reflections("com.dank1234.plugin", new TypeAnnotationsScanner());
        Logger.logRaw("[Bootstrap | Commands] Scanning 'com.dank1234.plugin' for all commands.");

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Cmd.class, true);

        if (annotatedClasses.isEmpty()) {
            Logger.logRaw("[Bootstrap | Commands] No commands found.");
            return;
        }

        List<String> classNames = new ArrayList<>();
        for (Class<?> clazz : annotatedClasses) {
            classNames.add(clazz.getSimpleName());
        }
        Logger.logRaw("[Bootstrap | Commands] Found commands: " + String.join(", ", classNames));

        for (Class<?> clazz : annotatedClasses) {
            try {
                if (ICommand.class.isAssignableFrom(clazz)) {
                    ICommand cmd = (ICommand) clazz.getDeclaredConstructor().newInstance();

                    Cmd cmdAnnotation = clazz.getAnnotation(Cmd.class);
                    if (cmdAnnotation != null) {
                        if ((cmdAnnotation.server() != ServerType.GLOBAL) && currentServer != cmdAnnotation.server()) {
                            continue;
                        }

                        cmd.names(cmdAnnotation.names());
                        cmd.permissions(cmdAnnotation.perms());

                        for (String name : cmdAnnotation.names()) {
                            commandHandlers.put(name, cmd);
                            registerBukkitCommand(name, cmdAnnotation);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void autoRegisterListeners() {
        Reflections reflections = new Reflections("com.dank1234.plugin", new TypeAnnotationsScanner());
        Logger.logRaw("[Bootstrap | Events] Scanning 'com.dank1234.plugin' for all events.");

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Event.class, true);

        if (annotatedClasses.isEmpty()) {
            Logger.logRaw("[Bootstrap | Events] No events found.");
            return;
        }

        List<String> classNames = new ArrayList<>();
        for (Class<?> clazz : annotatedClasses) {
            classNames.add(clazz.getSimpleName());
        }
        Logger.logRaw("[Bootstrap | Events] Found events: " + String.join(", ", classNames));

        for (Class<?> clazz : annotatedClasses) {
            try {
                if (Listener.class.isAssignableFrom(clazz)) {
                    Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                    Event eventAnnotation = clazz.getAnnotation(Event.class);

                    Bukkit.getPluginManager().registerEvents(listener, Main.get());
                    registeredListeners.add(listener); // Track the listener
                    Logger.logRaw("[Bootstrap | Events] Event[Server=" + eventAnnotation.server().name() + "] [Name=" + clazz.getSimpleName() + "] registered!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterCommands() {
        Reflections reflections = new Reflections("com.dank1234.plugin", new TypeAnnotationsScanner());
        Logger.logRaw("[Bootstrap | Commands] Scanning 'com.dank1234.plugin' for all commands.");

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Cmd.class, true);
        for (Class<?> clazz : annotatedClasses) {
            try {
                if (ICommand.class.isAssignableFrom(clazz)) {
                    ICommand cmd = (ICommand) clazz.getDeclaredConstructor().newInstance();

                    Cmd cmdAnnotation = clazz.getAnnotation(Cmd.class);
                    if (cmdAnnotation != null) {
                        cmd.names(cmdAnnotation.names());
                        cmd.permissions(cmdAnnotation.perms());

                        for (String name : cmdAnnotation.names()) {
                            commandHandlers.put(name, cmd);
                            unregisterBukkitCommand(name);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commandHandlers.clear();
        Logger.logRaw("[Bootstrap | Commands] Unregistered all commands.");
    }
    public void unregisterListeners() {
        for (Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
        registeredListeners.clear();
        Logger.logRaw("[Bootstrap | Events] Unregistered all events.");
    }

    private void registerBukkitCommand(String name, Cmd cmdAnnotation) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            BukkitCommand command = new BukkitCommand(name) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
                    if (cmdAnnotation.playerOnly() && !(sender instanceof Player)) {
                        Message.create(Bukkit.getConsoleSender(), Locale.PLAYER_ONLY_COMMAND).send();
                        return false;
                    }
                    return Register.get().register(sender, this, commandLabel, args);
                }
            };
            List<String> aliases = new ArrayList<>(Arrays.stream(cmdAnnotation.names()).toList());
            aliases.remove(name);

            command.setAliases(aliases);
            command.setPermission(String.join(",", cmdAnnotation.perms()));

            commandMap.register(Main.get().getName(), command);
            Logger.logRaw("[Bootstrap | Commands] Command[Server="+cmdAnnotation.server().name()+"] [Name="+name+"] [Disabled="+cmdAnnotation.disabled()+"] registered!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean register(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        ICommand handler = null;
        try {
            if (commandHandlers.containsKey(command.getName().toLowerCase())) {
                handler = commandHandlers.get(command.getName().toLowerCase());

                handler.player(sender instanceof Player ? (Player) sender : null);
                handler.sender(sender);
                handler.args(args);

                if (disabled(handler)) {
                    Message.create(MessageType.ERROR, handler.sender(), "This command is disabled!").send();
                    return false;
                }

                handler.execute(sender, args);
            }
        } catch (Exception e) {
            assert handler != null;
            Message.create(MessageType.EXCEPTION, handler.sender(), "An error occurred while executing this command!\n" + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
    private void unregisterBukkitCommand(String commandName) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            Command command = commandMap.getCommand(commandName);
            if (command != null) {
                command.unregister(commandMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean disabled(ICommand command) {
        Cmd cmd = command.getClass().getAnnotation(Cmd.class);
        return cmd != null && cmd.disabled();
    }
}