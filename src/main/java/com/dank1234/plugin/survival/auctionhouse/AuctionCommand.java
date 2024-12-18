package com.dank1234.plugin.survival.auctionhouse;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.data.database.AuctionManager;
import com.dank1234.api.server.ServerType;
import com.dank1234.api.wrapper.item.Item;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Command(
        server = ServerType.SURVIVAL,
        names = {"ah", "auctionhouse", "auction-house", "market", "auction"},
        playerOnly = true
)
public class AuctionCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            AuctionManager.openCategoryList(user);
        } else if (args.length == 3 && args(0).equals("sell")) { // /ah sell tokens 100
            Item item = user.getHeldItem();
            user.getHeldItem().toBukkit().setType(Material.AIR);

            Economy ecoType = Economy.valueOf(args(1).toUpperCase());
            double price = Integer.parseInt(args(2));

            AuctionItem auctionItem = new AuctionItem(AuctionManager.generateAuctionID(), user, item, ecoType, price);
            AuctionManager.insert(auctionItem);

            user.sendMessage("&aListed &f"+item.amount()+"x "+item.displayName() +"&a on the auction house for &f"+price+" &a"+ecoType.getName()+".");
        }
    }
}
