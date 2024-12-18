package com.dank1234.api.command;

import com.dank1234.plugin.Main;
import com.dank1234.api.Locale;
import com.dank1234.api.Logger;
import com.dank1234.api.server.ServerType;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.Bukkit;
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

@Deprecated(forRemoval = true)
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
        Logger.infoRaw("[Bootstrap | Commands] Scanning 'com.dank1234.plugin' for all commands.");

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Command.class, true);

        if (annotatedClasses.isEmpty()) {
            Logger.infoRaw("[Bootstrap | Commands] No commands found.");
            return;
        }

        List<String> classNames = new ArrayList<>();
        for (Class<?> clazz : annotatedClasses) {
            classNames.add(clazz.getSimpleName());
        }
        Logger.infoRaw("[Bootstrap | Commands] Found commands: " + String.join(", ", classNames));

        for (Class<?> clazz : annotatedClasses) {
            try {
                if (ICommand.class.isAssignableFrom(clazz)) {
                    ICommand cmd = (ICommand) clazz.getDeclaredConstructor().newInstance();

                    Command commandAnnotation = clazz.getAnnotation(Command.class);
                    if (commandAnnotation != null) {
                        if ((commandAnnotation.server() != ServerType.GLOBAL) && currentServer != commandAnnotation.server()) {
                            continue;
                        }
                        cmd.names(commandAnnotation.names());

                        for (String name : commandAnnotation.names()) {
                            commandHandlers.put(name, cmd);
                            registerBukkitCommand(name, commandAnnotation);
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

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Event.class, true);

        if (annotatedClasses.isEmpty()) {
            Logger.infoRaw("[Bootstrap | Events] No events found.");
            return;
        }

        for (Class<?> clazz : annotatedClasses) {
            try {
                if (Listener.class.isAssignableFrom(clazz)) {
                    Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();

                    Bukkit.getPluginManager().registerEvents(listener, Main.get());
                    registeredListeners.add(listener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterCommands() {
        Reflections reflections = new Reflections("com.dank1234.plugin", new TypeAnnotationsScanner());
        Logger.infoRaw("[Bootstrap | Commands] Scanning 'com.dank1234.plugin' for all commands.");

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Command.class, true);
        for (Class<?> clazz : annotatedClasses) {
            try {
                if (ICommand.class.isAssignableFrom(clazz)) {
                    ICommand cmd = (ICommand) clazz.getDeclaredConstructor().newInstance();

                    Command commandAnnotation = clazz.getAnnotation(Command.class);
                    if (commandAnnotation != null) {
                        cmd.names(commandAnnotation.names());

                        for (String name : commandAnnotation.names()) {
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
        Logger.infoRaw("[Bootstrap | Commands] Unregistered all commands.");
    }
    public void unregisterListeners() {
        for (Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
        registeredListeners.clear();
        Logger.infoRaw("[Bootstrap | Events] Unregistered all events.");
    }

    private void registerBukkitCommand(String name, Command commandAnnotation) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            BukkitCommand command = new BukkitCommand(name) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
                    if (commandAnnotation.playerOnly() && !(sender instanceof Player)) {
                        User.of(sender.getName()).sendMessage(Locale.PLAYER_ONLY_COMMAND);
                        return false;
                    }
                    return Register.get().register(sender, this, commandLabel, args);
                }
            };
            List<String> aliases = new ArrayList<>(Arrays.stream(commandAnnotation.names()).toList());
            aliases.remove(name);
            command.setAliases(aliases);

            commandMap.register(Main.get().getName(), command);
            Logger.infoRaw("[Bootstrap | Commands] Command[Server="+ commandAnnotation.server().name()+"] [Name="+name+"] [Disabled="+ commandAnnotation.disabled()+"] registered!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean register(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        ICommand handler = null;
        try {
            if (commandHandlers.containsKey(command.getName().toLowerCase())) {
                handler = commandHandlers.get(command.getName().toLowerCase())
                        .setSender(sender)
                        .args(args);
                User user = handler.sender();

                if (handler.disabled()) {
                    user.sendMessage(Locale.DISABLED_COMMAND);
                    return false;
                }
                handler.execute(user, handler.args());
            }
        } catch (Exception e) {
            if (handler != null) {
                handler.sender().sendMessage(Locale.EXCEPTION_THROWN);
            }
            Logger.info(Locale.EXCEPTION_THROWN);
            e.printStackTrace();
        }
        return true;
    }
    private void unregisterBukkitCommand(String commandName) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            org.bukkit.command.Command command = commandMap.getCommand(commandName);
            if (command != null) {
                command.unregister(commandMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}