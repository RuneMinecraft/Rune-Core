package com.dank1234.plugin;

import com.dank1234.utils.command.Register;
import com.dank1234.utils.data.Config;
import com.dank1234.utils.data.Version;
import com.dank1234.utils.server.Server;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class Main extends JavaPlugin {
    private final Bootstrap bootstrap = new Bootstrap();

    private static Main instance;
    public static Main get() {
        return instance;
    }

    public Version version(){
        return this.bootstrap.version;
    }
    public Server server() {
        return this.bootstrap.server;
    }
    public Register register() {
        return this.bootstrap.register;
    }
    public Config config() {
        return this.bootstrap.config;
    }

    @Override
    public void onLoad() {
        instance = this;

        this.setNaggable(false);
        //bootstrap.load();
    }

    @Override
    public void onEnable() {
        bootstrap.load();
        bootstrap.enable();
        try {
            System.out.println(Class.forName("net.minecraft.server.level.ServerLevel"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        bootstrap.disable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Message.create(Bukkit.getConsoleSender(), sender.getName()+" executed the command '/"+label+" "+Arrays.toString(Arrays.copyOfRange(args, 0, args.length))+"'").send();
        return this.register().register(sender, command, label, args);
    }
}