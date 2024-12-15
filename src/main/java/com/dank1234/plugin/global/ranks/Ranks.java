package com.dank1234.plugin.global.ranks;

import com.dank1234.utils.server.ServerType;

import java.sql.SQLException;

public enum Ranks {
    // ᴀ ʙ

    MANAGER(ServerType.GLOBAL, Group.Companion.get("Manager", "&#ff63c8&lᴍᴀɴᴀɢᴇʀ", null, 150)),
    DEVELOPER(ServerType.GLOBAL, Group.Companion.get("Dev", "&#ffb300&lᴅᴇᴠ", null, 140)),
    ADMIN(ServerType.GLOBAL, Group.Companion.get("Admin", "&#e32d2d&lᴀᴅᴍɪɴ", null, 130)),
    SR_MOD(ServerType.GLOBAL, Group.Companion.get("SrMod", "&#903dfc&lsʀ ᴍᴏᴅ", null, 120)),
    MOD(ServerType.GLOBAL, Group.Companion.get("Mod", "&#1ec5fc&lᴍᴏᴅ", null, 110)),
    HELPER(ServerType.GLOBAL, Group.Companion.get("Helper", "&#02f543&lʜᴇʟᴘᴇʀ", null, 100)),

    BUILDER(ServerType.GLOBAL, Group.Companion.get("Builder", "&#fcd33f&lʙᴜɪʟᴅᴇʀ", null, 90)),
    CREATOR(ServerType.GLOBAL, Group.Companion.get("Creator", "&#ff0000&lʏᴏᴜ&f&lᴛᴜʙᴇ", null, 80));

    // MASTER(ServerType.GLOBAL, Group.Companion.get("Master", "", null, 70)),
    // WARLOCK(ServerType.GLOBAL, Group.Companion.get("Warlock", "", null, 60)),
    // SORCERER(ServerType.GLOBAL, Group.Companion.get("Sorcerer", "", null, 50)),
    // WIZARD(ServerType.GLOBAL, Group.Companion.get("Wizard", "", null, 40)),
    // MAGE(ServerType.GLOBAL, Group.Companion.get("Mage", "", null, 30)),
    // MEMBER(ServerType.GLOBAL, Group.Companion.get("Member", "", null, 20));

    private final ServerType server;
    private final Group group;

    Ranks(ServerType server, Group group) {
        this.server = server;
        this.group = group;
    }

    public ServerType server() {
        return this.server;
    }
    public Group group() {
        return this.group;
    }

    public static void create() {
        for (Ranks r : Ranks.values()) {
            Group rank = r.group();
        }
    }
}
