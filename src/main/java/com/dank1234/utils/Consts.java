package com.dank1234.utils;

import com.dank1234.plugin.global.ranks.Group;
import com.dank1234.utils.server.Server;
import com.dank1234.utils.server.ServerType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

public final class Consts {
    public static final @NotNull Group MEMBER_GROUP;

    static {
        try {
            MEMBER_GROUP = Objects.requireNonNull(Group.Companion.get("member"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static final @NotNull Server DEFAULT_SERVER = Server.of(ServerType.GLOBAL, 0, "The default server. An error has occurred in the code.");

    public static final @NotNull String JDBC_URL = "jdbc:mariadb://localhost:3306/rune_database";
    public static final @NotNull String USERNAME = "sftpUser";
    public static final @NotNull String PASSWORD = "B24Z69K11M.";

    private static final String MOJANG_PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String MOJANG_SESSION_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
}
