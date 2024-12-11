package com.dank1234.plugin.survival.auctionhouse;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.utils.wrapper.item.Item;

public record AuctionItem(String auctionId, User seller, Item item, Economy type, double price) {}