package com.dank1234.utils.wrapper.player;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.utils.Utils;
import com.dank1234.utils.data.database.EcoManager;
import com.dank1234.utils.data.database.UserManager;
import com.dank1234.utils.wrapper.inventory.Menu;
import com.dank1234.utils.wrapper.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

@Nullable
public class User implements Utils {
    private final UUID uuid;
    private final String username;

    private double gems;
    private double tokens;
    private double souls;

    protected User(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;

        this.updateEconomy();
    }
    public static User of(UUID uuid, String username) {
        return new User(uuid, username);
    }
    public static User of(UUID uuid) {
        return UserManager.getUser(uuid).orElse(null);
    }
    public static User of(String username) {
        return UserManager.getUser(username).orElse(null);
    }

    public UUID uuid() {
        return this.uuid;
    }
    public String username() {
        return this.username;
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public GameMode gamemode() {
        return this.getPlayer().getGameMode();
    }
    public User setGameMode(GameMode gm) {
        this.getPlayer().setGameMode(gm);
        return this;
    }
    public User sudo(String cmd) {
        if (cmd.startsWith("c://") || cmd.startsWith("C://")) this.getPlayer().chat(cmd.substring(4));
        else this.getPlayer().performCommand(cmd);
        return this;
    }

    public double getEco(Economy eco) {
        return switch (eco) {
            case GEMS -> this.gems();
            case SOULS -> this.souls();
            case TOKENS -> this.tokens();
        };
    }
    public User setEco(Economy eco, double value) {
        switch (eco) {
            case GEMS -> this.gems = value;
            case TOKENS -> this.tokens = value;
            case SOULS -> this.souls = value;
        }
        EcoManager.setValue(this.uuid, eco.getName(), value);
        return this;
    }
    public double gems() {
        return this.gems;
    }
    public User setGems(double gems) {
        this.gems = gems;
        EcoManager.setValue(this.uuid(), "gems", gems);
        System.out.println("set gems");
        return this;
    }
    public double tokens() {
        return this.tokens;
    }
    public User setTokens(double tokens) {
        this.tokens = tokens;
        EcoManager.setValue(this.uuid(), "tokens", tokens);
        System.out.println("set tokens");
        return this;
    }
    public double souls() {
        return this.souls;
    }
    public User setSouls(double souls) {
        this.souls = souls;
        EcoManager.setValue(this.uuid(), "souls", souls);
        System.out.println("set souls");
        return this;
    }
    public User updateEconomy() {
        this.gems = EcoManager.getValue(this.uuid, "gems").orElse(0.0);
        this.tokens = EcoManager.getValue(this.uuid, "tokens").orElse(0.0);
        this.souls = EcoManager.getValue(this.uuid, "souls").orElse(0.0);
        return this;
    }

    public Menu openMenu(Menu menu) {
        if (getPlayer() != null) {
            getPlayer().openInventory(menu.inventory());
        }
        return menu;
    }
    public User closeMenu() {
        if (getPlayer() != null) {
            getPlayer().closeInventory();
        }
        return this;
    }

    public Item getHeldItem() {
        ItemStack itemStack = this.getPlayer().getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return null;

        return Item.of(itemStack);
    }
    public User giveItem(Item item) {
        this.getPlayer().getInventory().addItem(item.toBukkit());
        return this;
    }

    public Player getPlayer() {
        Player player = Bukkit.getPlayer(this.uuid());
        if (player == null) {
            throw new IllegalStateException("Player is not online. Cannot retrieve player.");
        }

        return player;
    }
    public CommandSender getCommandSender() {
        CommandSender player = Bukkit.getPlayer(this.uuid());
        if (player == null) {
            throw new IllegalStateException("Player is not online. Cannot retrieve command sener.");
        }

        return player;
    }

    @Override
    public String toString() {
        return
                "User[\n" +
                    "    name: "+this.username()+"\n"+
                    "    uuid: "+this.uuid()+"\n"+
                "]";
    }
}
