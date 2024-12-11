package com.dank1234.plugin.survival.auctionhouse;

import com.dank1234.utils.Locale;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.data.database.AuctionManager;
import com.dank1234.utils.wrapper.item.Item;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

@Event
public class AuctionListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(Locale.AUCTION_GUI_TITLE)) return;
        event.setCancelled(true);

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        User user = User.of(event.getWhoClicked().getUniqueId());
        Item clickedItem = Item.create(
                event.getCurrentItem().getType(),
                event.getCurrentItem().getItemMeta().getDisplayName(),
                event.getCurrentItem().getAmount(),
                event.getCurrentItem().getItemMeta().getLore().toArray(String[]::new)
        );

        String auctionIdLine = clickedItem.lore().get(7);
        if (!auctionIdLine.startsWith("§8Auction ID: ")) return;

        Optional<AuctionItem> auctionItem = AuctionManager.getAuction(auctionIdLine.substring("§8Auction ID: ".length()));
        if (auctionItem.isEmpty()) {
            Message.create(user, "&cThis auction item no longer exists.").send();
            return;
        }
        System.out.println(auctionItem.get().seller().getUuid());
        System.out.println(user.getUuid());
        if (auctionItem.get().seller().getUuid().toString().equals(user.getUuid().toString())) {
            Message.create(user, "&cYou cannot buy your own auctions.").send();
            return;
        }

        AuctionManager.handlePurchase(user, auctionItem.get());
    }
}
