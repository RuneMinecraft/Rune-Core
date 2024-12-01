package com.dank1234.utils.wrapper.message;

import com.dank1234.utils.Logger;
import com.dank1234.utils.Utils;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class Message implements Utils {
    private final MessageType type;
    private final Set<CommandSender> players;
    private final Set<User> users;
    private final String[] messages;
    private final Component[] cMessages;

    private Message(MessageType type, Set<?> input, String ... messages) {
        this.type = type;
        this.messages = messages;
        this.cMessages = new Component[0];

        this.players = new HashSet<>();
        this.users = new HashSet<>();

        if (!input.isEmpty() && input.iterator().next() instanceof CommandSender) {
            input.forEach(item -> {
                CommandSender player = (CommandSender) item;
                this.players.add(player);
                this.users.add(User.of(player.getName()));
            });
        } else if (!input.isEmpty() && input.iterator().next() instanceof User) {
            input.forEach(item -> {
                User user = (User) item;
                this.users.add(user);
                this.players.add(user.getPlayer());
            });
        }
    }
    private Message(MessageType type, Set<?> input, Component ... messages) {
        this.type = type;
        this.cMessages = messages;
        this.messages = new String[0];

        this.players = new HashSet<>();
        this.users = new HashSet<>();

        if (!input.isEmpty() && input.iterator().next() instanceof CommandSender) {
            input.forEach(item -> {
                CommandSender player = (CommandSender) item;
                this.players.add(player);
                this.users.add(User.of(player.getName()));
            });
        } else if (!input.isEmpty() && input.iterator().next() instanceof User) {
            input.forEach(item -> {
                User user = (User) item;
                this.users.add(user);
                this.players.add(user.getPlayer());
            });
        }
    }

    public static Message create(MessageType type, CommandSender player, String ... messages) {
        return create(type, Collections.singleton(player).toArray(CommandSender[]::new), messages);
    }
    public static Message create(CommandSender player, String ... messages) {
        return Message.create(MessageType.NORMAL, player, messages);
    }
    public static Message create(MessageType type, CommandSender[] players, String ... messages) {
        return new Message(type, Arrays.stream(players).collect(Collectors.toSet()), messages);
    }
    public static Message create(CommandSender[] players, String ... messages) {
        return create(MessageType.NORMAL, players, messages);
    }
    public static Message create(MessageType type, CommandSender player, Component... messages) {
        return create(type, Collections.singleton(player).toArray(CommandSender[]::new), messages);
    }
    public static Message create(CommandSender player, Component ... messages) {
        return Message.create(MessageType.NORMAL, player, messages);
    }
    public static Message create(MessageType type, CommandSender[] players, Component ... messages) {
        return new Message(type, Arrays.stream(players).collect(Collectors.toSet()), messages);
    }
    public static Message create(CommandSender[] players, Component ... messages) {
        return create(MessageType.NORMAL, players, messages);
    }

    public static Message create(MessageType type, User user, String ... messages) {
        return Message.create(type, Collections.singleton(user).toArray(User[]::new), messages);
    }
    public static Message create(User user, String ... messages) {
        return Message.create(MessageType.NORMAL, user, messages);
    }
    public static Message create(MessageType type, User[] users, String ... messages) {
        return new Message(type, Arrays.stream(users).collect(Collectors.toSet()), messages);
    }
    public static Message create(User[] users, String ... messages) {
        return Message.create(MessageType.NORMAL, users, messages);
    }
    public static Message create(MessageType type, User user, Component ... messages) {
        return Message.create(type, Collections.singleton(user).toArray(User[]::new), messages);
    }
    public static Message create(User user, Component ... messages) {
        return Message.create(MessageType.NORMAL, user, messages);
    }
    public static Message create(MessageType type, User[] users, Component ... messages) {
        return new Message(type, Arrays.stream(users).collect(Collectors.toSet()), messages);
    }
    public static Message create(User[] users, Component ... messages) {
        return Message.create(MessageType.NORMAL, users, messages);
    }

    public static Message broadcast(MessageType type, String ... messages) {
        return create(type, Bukkit.getOnlinePlayers().toArray(CommandSender[]::new), messages);
    }
    public static Message broadcast(String ... messages) {
        return broadcast(MessageType.NORMAL, messages);
    }
    public static Message broadcast(MessageType type, Component ... messages) {
        return create(type, Bukkit.getOnlinePlayers().toArray(CommandSender[]::new), messages);
    }
    public static Message broadcast(Component ... messages) {
        return broadcast(MessageType.NORMAL, messages);
    }

    public MessageType type() {
        return this.type;
    }
    public Set<CommandSender> players() {
        return this.players;
    }
    public Set<User> users() {
        return this.users;
    }
    public String[] messages() {
        return this.messages;
    }
    public Component[] cMessages() {
        return this.cMessages;
    }

    public void send() {
        if (players == null || users == null) {
            Logger.log("Failed to send a message. 'Set<CommandSender> players' is null.");
            return;
        }
        if (messages != null) {
            for (CommandSender player : this.players()) {
                Arrays.stream(this.messages()).toList().forEach(message -> player.sendMessage(Colour(this.type() + message)));
            }
        }
        if (cMessages != null) {
            for (CommandSender player : this.players()) {
                Arrays.stream(this.cMessages()).toList().forEach(player::sendMessage);
            }
        }
    }
    public void send(boolean log) {
        this.send();
        if (log) {
            Logger.log(this.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder playersNames = new StringBuilder();
        for (CommandSender player : players) {
            playersNames.append(player.getName());
            playersNames.append(", ");
        }

        if (!playersNames.isEmpty()) {
            playersNames.setLength(playersNames.length() - 2);
        }

        return "new Message() {\n" +
                "\ttype: " + type.name() + ",\n" +
                "\tplayers: " + playersNames + ",\n" +
                "\tmessages: " + Arrays.toString(messages) +
                "\tcMessages: " + Arrays.toString(cMessages) +
                "\n}";
    }
}