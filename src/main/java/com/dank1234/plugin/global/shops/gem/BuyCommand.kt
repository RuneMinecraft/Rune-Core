package com.dank1234.plugin.global.shops.gem

import com.dank1234.plugin.global.shops.gem.subshops.*
import com.dank1234.utils.command.Command
import com.dank1234.utils.command.ICommand
import com.dank1234.utils.wrapper.inventory.Menu
import com.dank1234.utils.wrapper.item.Item
import com.dank1234.utils.wrapper.player.User
import org.bukkit.Material

@Command(names = ["buy"])
class BuyCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        val menu = Menu.create("Gem Shop", 27)
            .setItem(9, Item.create(Material.NETHER_STAR, "&b&lRanks", 1, " ", "&7-----------------------", "&7| Click here to purchase ranks."))
            .setItem(10, Item.create(Material.SUNFLOWER, "&c&lRunePass", 1, " ", "&7-----------------------", "&7| Click here to purchase the pass."))
            .setItem(11, Item.create(Material.NAME_TAG, "&d&lBadges", 1, " ", "&7-----------------------", "&7| Click here to purchase badges."))
            .setItem(12, Item.create(Material.BLUE_WOOL, "&e&lColours", 1, " ", "&7-----------------------", "&7| Click here to purchase colours."))
            .setItem(14, Item.create(Material.OMINOUS_BOTTLE, "&4&lEffects", 1, " ", "&7-----------------------", "&7| Click here to purchase effects."))
            .setItem(15, Item.create(Material.REDSTONE, "&3&lParticles", 1, " ", "&7-----------------------", "&7| Click here to purchase particles."))
            .setItem(16, Item.create(Material.SKELETON_SKULL, "&6&lPets", 1, " ", "&7-----------------------", "&7| Click here to purchase pets."))
            .setItem(17, Item.create(Material.GLOWSTONE, "&a&lGlows", 1, " ", "&7-----------------------", "&7| Click here to purchase glows."))
            .setAction(9) { it: User -> user.openMenu(ranksMenu(it)) }
            .setAction(10) { it: User -> user.openMenu(passMenu(it)) }
            .setAction(11) { it: User -> user.openMenu(badgesMenu(it)) }
            .setAction(12) { it: User -> user.openMenu(coloursMenu(it)) }
            .setAction(14) { it: User -> user.openMenu(effectsMenu(it)) }
            .setAction(15) { it: User -> user.openMenu(particlesMenu(it)) }
            .setAction(16) { it: User -> user.openMenu(petsMenu(it)) }
            .setAction(17) { it: User -> user.openMenu(glowsMenu(it)) }
        .open(user)
    }
}