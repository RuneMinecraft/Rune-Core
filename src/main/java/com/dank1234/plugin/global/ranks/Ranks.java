package com.dank1234.plugin.global.ranks;

import com.dank1234.utils.server.ServerType;

import java.sql.SQLException;

public enum Ranks {
    MANAGER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Manager", "&#ff63c8&lᴍᴀɴᴀ", null, 100);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    DEVELOPER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Dev", "&#ffb300&lᴅᴇᴠ", null, 90);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    ADMIN(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Admin", "&#e32d2d&lᴀᴅᴍɪɴ", null, 80);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    SR_MOD(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("SrMod", "&#903dfc&lsʀ ᴍᴏᴅ", null, 80);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    MOD(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Mod", "&#1ec5fc&lᴍᴏᴅ", null, 80);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    HELPER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Helper", "&#02f543&lʜᴇʟᴘᴇʀ", null, 80);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),
    BUILDER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Builder", "&#fcd33f&lʙᴜɪʟᴅᴇʀ", null, 80);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    CREATOR(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Creator", "&#ff0000&lʏᴏᴜ&f&lᴛᴜʙᴇ", null, 80);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    MASTER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Master", "", null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    WARLOCK(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Warlock", "", null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    SORCERER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Sorcerer", "", null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    WIZARD(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Wizard", "", null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    MAGE(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Mage", "", null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate()),

    MEMBER(ServerType.GLOBAL, new Object() {
        Group evaluate() {
            try {
                return Group.Companion.get("Member", "", null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }.evaluate());

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
