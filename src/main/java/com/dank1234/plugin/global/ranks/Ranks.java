package com.dank1234.plugin.global.ranks;

import com.dank1234.api.server.ServerType;

public enum Ranks {
    // ᴀ ʙ ᴄ ᴅ ᴇ ғ ɢ ʜ ɪ ᴊ ᴋ ʟ ᴍ ɴ ᴏ ᴘ ǫ ʀ s ᴛ ᴜ ᴠ ᴡ x ʏ ᴢ

    MANAGER(ServerType.GLOBAL, RankType.STAFF, Group.Companion.get("Manager", "&#ff63c8&l"+"ᴍᴀɴᴀɢᴇʀ", null, 150)),
    DEVELOPER(ServerType.GLOBAL, RankType.STAFF, Group.Companion.get("Dev", "&#ffb300&l"+"ᴅᴇᴠ", null, 140)),
    ADMIN(ServerType.GLOBAL, RankType.STAFF, Group.Companion.get("Admin", "&#e32d2d&l"+"ᴀᴅᴍɪɴ", null, 130)),
    SR_MOD(ServerType.GLOBAL, RankType.STAFF, Group.Companion.get("SrMod", "&#903dfc&l"+"sʀ ᴍᴏᴅ", null, 120)),
    MOD(ServerType.GLOBAL, RankType.STAFF, Group.Companion.get("Mod", "&#1ec5fc&l"+"ᴍᴏᴅ", null, 110)),
    HELPER(ServerType.GLOBAL, RankType.STAFF, Group.Companion.get("Helper", "&#02f543&l"+"ʜᴇʟᴘᴇʀ", null, 100)),

    BUILDER(ServerType.GLOBAL, RankType.MISC, Group.Companion.get("Builder", "&#fcd33f&l"+"ʙᴜɪʟᴅᴇʀ", null, 90)),
    CREATOR(ServerType.GLOBAL, RankType.MISC, Group.Companion.get("Creator", "&#ff0000&l"+"ʏᴏᴜ&f&lᴛᴜʙᴇ", null, 80)),

    MASTER(ServerType.GLOBAL, RankType.MEMBER, Group.Companion.get("Master", "&#a237f6&l"+"ᴍᴀsᴛᴇʀ", null, 70)),
    WARLOCK(ServerType.GLOBAL, RankType.MEMBER, Group.Companion.get("Warlock", "&#fa573d&l"+"ᴡᴀʀʟᴏᴄᴋ", null, 60)),
    SORCERER(ServerType.GLOBAL, RankType.MEMBER, Group.Companion.get("Sorcerer", "#3caff5&l"+"sᴏʀᴄᴇʀᴇʀ", null, 50)),
    WIZARD(ServerType.GLOBAL, RankType.MEMBER, Group.Companion.get("Wizard", "&#88f03e&l"+"ᴡɪᴢᴀʀᴅ", null, 40)),
    MAGE(ServerType.GLOBAL, RankType.MEMBER, Group.Companion.get("Mage", "&#a237f6&l"+"ᴍᴀɢᴇ", null, 30)),
    MEMBER(ServerType.GLOBAL, RankType.MEMBER, Group.Companion.get("Member", "&7&l"+"ᴍᴇᴍʙᴇʀ", null, 20));

    private final ServerType server;
    private final RankType rankType;
    private final Group group;

    Ranks(ServerType server, RankType rankType, Group group) {
        this.server = server;
        this.rankType = rankType;
        this.group = group;
    }

    public ServerType server() {
        return this.server;
    }
    public RankType rankType() {
        return this.rankType;
    }
    public Group group() {
        return this.group;
    }

    public enum RankType {
        MEMBER,
        MISC,
        STAFF
    }
}
