package com.dank1234.api.data.database;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.plugin.survival.auctionhouse.AuctionCategory;
import com.dank1234.plugin.survival.auctionhouse.AuctionItem;
import com.dank1234.api.Locale;
import com.dank1234.api.Utils;
import com.dank1234.api.wrapper.inventory.Menu;
import com.dank1234.api.wrapper.item.Item;
import com.dank1234.api.wrapper.item.Items;
import com.dank1234.api.wrapper.message.Message;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.*;

public class AuctionManager extends SQLUtils implements Utils {
    private static final String TABLE = "auctions";

    public static void ensureTableExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS auctions (
                    auction_id   VARCHAR(36) PRIMARY KEY,
                    seller       VARCHAR(36) NOT NULL,
                    item         BLOB NOT NULL,
                    price        DOUBLE NOT NULL,
                    economy_type VARCHAR(50) NOT NULL,
                    FOREIGN KEY (seller) REFERENCES users(uuid) ON DELETE CASCADE
                );
            """;
        executeUpdate(sql);
    }

    public static void insert(AuctionItem auctionItem) {
        try {
            String sql = "INSERT INTO auctions (auction_id, seller, item, price, economy_type) VALUES (?, ?, ?, ?, ?);";

            executeUpdate(sql, pstmt -> {
                pstmt.setString(1, auctionItem.auctionId());
                pstmt.setString(2, auctionItem.seller().getUuid().toString());
                pstmt.setBytes(3, Item.serializeItem(auctionItem.item()));
                pstmt.setDouble(4, auctionItem.price());
                pstmt.setString(5, auctionItem.type().name());
            });
        }catch (Exception e) {}
    }
    public static void delete(AuctionItem auctionItem) {
        executeUpdate("DELETE FROM auctions WHERE auction_id = ?;", pstmt -> {
            try {
                pstmt.setString(1, auctionItem.auctionId());
            } catch (SQLException e) {
                //throw new RuntimeException(e);
            }
        });
    }
    public static Optional<AuctionItem> getAuction(String auctionId) {
        try {
            String sql = "SELECT auction_id, seller, item, price, economy_type FROM " + TABLE + " WHERE auction_id = ?;";

            return executeQuery(sql, pstmt -> {
                try {
                    pstmt.setString(1, auctionId);
                } catch (SQLException e) {
                    //throw new RuntimeException(e);
                }
            }, rs -> {
                if (rs.next()) {
                    String auctionID = rs.getString("auction_id");
                    UUID sellerUUID = UUID.fromString(rs.getString("seller"));
                    Item item = Item.deserializeItem(rs.getBytes("item"));
                    double price = rs.getDouble("price");
                    Economy economyType = Economy.valueOf(rs.getString("economy_type"));

                    User seller = User.of(sellerUUID);
                    return new AuctionItem(auctionID, seller, item, economyType, price);
                }
                return null;
            });
        }catch(Exception e){}
        return Optional.empty();
    }
    public static Optional<List<AuctionItem>> getAllAuctions() {
        try {
            String sql = "SELECT auction_id, seller, item, price, economy_type FROM " + TABLE + ";";

            return executeQuery(sql, pstmt -> {
            }, rs -> {
                List<AuctionItem> auctionItems = new ArrayList<>();

                while (rs.next()) {
                    String auctionID = rs.getString("auction_id");
                    UUID sellerUUID = UUID.fromString(rs.getString("seller"));
                    Item item = Item.deserializeItem(rs.getBytes("item"));
                    double price = rs.getDouble("price");
                    Economy economyType = Economy.valueOf(rs.getString("economy_type"));

                    User seller = User.of(sellerUUID);
                    AuctionItem auctionItem = new AuctionItem(auctionID, seller, item, economyType, price);
                    auctionItems.add(auctionItem);
                }

                return auctionItems.isEmpty() ? null : auctionItems;
            });
        }catch(Exception e){}
        return Optional.empty();
    }

    public static void handlePurchase(User user, AuctionItem auctionItem) {
        double userBalance = user.getEco(auctionItem.type());

        if (userBalance < auctionItem.price()) {
            Message.create(user, "&cYou do not have enough to purchase this item.").send();
            return;
        }

        user.setEco(auctionItem.type(), userBalance - auctionItem.price());
        user.giveItem(auctionItem.item());

        if (auctionItem.seller().isOnline()) {
            Message.create(auctionItem.seller(),
                    "&aSuccessfully sold an item from the auction house!",
                    "&a| Item Name: &e"+auctionItem.item().displayName(),
                    "&a| Type: &e"+ auctionItem.type(),
                    "&a| Price: &e$"+auctionItem.price()
            ).send();
        }
        AuctionManager.delete(auctionItem);

        Message.create(user, "&aSuccessfully purchased the item.").send();
        user.closeMenu();
    }

    public static void openCategoryList(User user) {
        Menu.create(Utils.sColour(Locale.AUCTION_GUI_TITLE), 54)
                .setItem(11, AuctionCategory.BLOCKS.item())
                .setItem(13, AuctionCategory.MINERALS.item())
                .setItem(15, AuctionCategory.COMBAT_AND_TOOLS.item())
                .setItem(21, AuctionCategory.FARMING.item())
                .setItem(23, AuctionCategory.MOB_DROPS.item())
                .setItem(29, AuctionCategory.FOOD.item())
                .setItem(31, AuctionCategory.REDSTONE.item())
                .setItem(33, AuctionCategory.VOUCHERS.item())
                .setItem(40, AuctionCategory.MISC.item())
                .setAction(11, (actionUser) -> openAuctions(AuctionCategory.BLOCKS, actionUser))
                .setAction(13, (actionUser) -> openAuctions(AuctionCategory.MINERALS, actionUser))
                .setAction(15, (actionUser) -> openAuctions(AuctionCategory.COMBAT_AND_TOOLS, actionUser))
                .setAction(21, (actionUser) -> openAuctions(AuctionCategory.FARMING, actionUser))
                .setAction(23, (actionUser) -> openAuctions(AuctionCategory.MOB_DROPS, actionUser))
                .setAction(29, (actionUser) -> openAuctions(AuctionCategory.FOOD, actionUser))
                .setAction(31, (actionUser) -> openAuctions(AuctionCategory.REDSTONE, actionUser))
                .setAction(33, (actionUser) -> openAuctions(AuctionCategory.VOUCHERS, actionUser))
                .setAction(40, (actionUser) -> openAuctions(AuctionCategory.MISC, actionUser))
                .fill(Material.LIME_STAINED_GLASS_PANE)
                .open(user);
    }
    public static void openAuctions(AuctionCategory category, User user) {
        Menu gui = Menu.create(Locale.AUCTION_GUI_TITLE + " | "+category.name(), 54);

        gui.fill(new int[]{1,2,3,4,5,6,7,8,10,17,19,26,28,35,37,44,46,48,49,50,51,53}, Material.LIME_STAINED_GLASS_PANE);
        gui.fill( new int[]{0,9,18,27,36,45}, Material.COBBLESTONE);

        gui.setItem(47, Items.BACK_ARROW.getItem());
        gui.setItem(52, Items.NEXT_ARROW.getItem());

        if (AuctionManager.getAllAuctions().isEmpty()) {
            user.openMenu(gui);
            return;
        }

        int slot = 0;
        for (AuctionItem auctionItem : AuctionManager.getAllAuctions().get()) {
            if (slot == 54) break;

            Item item = auctionItem.item();

            List<String> lore = item.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            List<String> addedLore = List.of(
                    Utils.sColour(" "),
                    Utils.sColour("&8>------------------------------<"),
                    Utils.sColour(" "),
                    Utils.sColour("&3&l| &bSeller: &f" + auctionItem.seller().getUsername()),
                    Utils.sColour("&3&l| &bPrice: &f" + auctionItem.price() +" "+ auctionItem.type().getName()),
                    Utils.sColour("&3&l| &bExpires In: &f" + "TODO"),
                    Utils.sColour(" "),
                    Utils.sColour("&8Auction ID: " + auctionItem.auctionId()),
                    Utils.sColour(" "),
                    Utils.sColour("&8>------------------------------<"),
                    Utils.sColour(" "),
                    Utils.sColour("&a&lCLICK FOR PURCHASE INFO")
            );
            lore.forEach((Utils::sColour));
            lore.addAll(addedLore);

            item.setLore(lore);

            gui.addItem(item);
            slot++;
        }

        user.openMenu(gui);
    }
    public static String generateAuctionID() {
        StringBuilder builder = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            int index = new Random().nextInt(characters.length());
            builder.append(characters.charAt(index));
        }
        return builder.toString();
    }
}