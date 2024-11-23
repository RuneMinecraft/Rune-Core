package com.dank1234.plugin.auctionhouse.commands;

import com.dank1234.plugin.survival.economy.Currency;
import com.dank1234.plugin.survival.economy.Souls;
import com.dank1234.plugin.survival.economy.Tokens;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.player.RunePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//comments are 4 pussies g 
// havent tested any of this btw but i am that good that i know it is going 2 work 

@Cmd(
    server = ServerType.SURVIVAL,
    names = {"ah", "auctionhouse", "auction-house", "market"}
)
public class AuctionHouse extends ICommand implements Listener {

    private final HashMap<UUID, AuctionItem> auctionItems = new HashMap<>();

    private final String AUCTION_GUI_TITLE = "Auction House";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use the Auction House.");
            return;
        }

        Player player = (Player) sender;

        openAuctionHouseGUI(player);
    }

    private void openAuctionHouseGUI(Player player) {
        Inventory auctionGUI = Bukkit.createInventory(null, 54, AUCTION_GUI_TITLE);

        int slot = 0;
        for (UUID auctionId : auctionItems.keySet()) {
            if (slot >= 54) break; 

            AuctionItem auctionItem = auctionItems.get(auctionId);
            ItemStack displayItem = auctionItem.getItemStack().clone();
            ItemMeta meta = displayItem.getItemMeta();

            meta.setLore(List.of(
                "§6Price (Souls): §e" + auctionItem.getPriceSouls(),
                "§bPrice (Tokens): §e" + auctionItem.getPriceTokens(),
                "§7Seller: §e" + Bukkit.getOfflinePlayer(auctionItem.getSeller()).getName(),
                "§8Auction ID: " + auctionId
            ));
            displayItem.setItemMeta(meta);

            auctionGUI.setItem(slot, displayItem);
            slot++;
        }

        player.openInventory(auctionGUI);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(AUCTION_GUI_TITLE)) return;

        event.setCancelled(true); 
        //dans a bellend
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || meta.getLore() == null) return;

        String auctionIdLine = meta.getLore().get(3);
        if (!auctionIdLine.startsWith("§8Auction ID: ")) return;

        String auctionIdStr = auctionIdLine.substring("§8Auction ID: ".length());
        UUID auctionId = UUID.fromString(auctionIdStr);

        AuctionItem auctionItem = auctionItems.get(auctionId);
        if (auctionItem == null) {
            player.sendMessage("§cThis auction item no longer exists.");
            return;
        }

        handlePurchase(player, auctionItem, auctionId);
    }

    private void handlePurchase(Player player, AuctionItem auctionItem, UUID auctionId) {
        RunePlayer runePlayer = RunePlayer.of(player);

        Souls souls = runePlayer.getCurrency(Souls.class);
        Tokens tokens = runePlayer.getCurrency(Tokens.class);

        if (souls.getBalance() < auctionItem.getPriceSouls() || tokens.getBalance() < auctionItem.getPriceTokens()) {
            player.sendMessage("§cYou don't have enough Souls or Tokens to purchase this item.");
            return;
        }

        souls.withdraw(auctionItem.getPriceSouls());
        tokens.withdraw(auctionItem.getPriceTokens());
        player.getInventory().addItem(auctionItem.getItemStack());
        //idek what im writing anymore icl 
        Player seller = Bukkit.getPlayer(auctionItem.getSeller());
        if (seller != null) {
            seller.sendMessage("§aYour item has been sold! You earned §6" + auctionItem.getPriceSouls() +
                    " Souls §aand §b" + auctionItem.getPriceTokens() + " Tokens.");
        }
        auctionItems.remove(auctionId);

        player.sendMessage("§aYou have successfully purchased the item!");
        player.closeInventory();
    }

    private static class AuctionItem {
        private final ItemStack itemStack;
        private final int priceSouls;
        private final int priceTokens;
        //i made so many typos trying to spell out the word private while doing this
        private final UUID seller;

        public AuctionItem(ItemStack itemStack, int priceSouls, int priceTokens, UUID seller) {
            this.itemStack = itemStack;
            this.priceSouls = priceSouls;
            this.priceTokens = priceTokens;
            this.seller = seller;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getPriceSouls() {
            return priceSouls;
        }

        public int getPriceTokens() {
            return priceTokens;
        }

        public UUID getSeller() {
            return seller;
        }
    }
}
