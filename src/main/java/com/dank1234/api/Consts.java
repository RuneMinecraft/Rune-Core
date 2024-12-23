package com.dank1234.api;

import com.dank1234.plugin.global.ranks.Group;
import com.dank1234.api.server.Server;
import com.dank1234.api.server.ServerType;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class Consts {
    public static final User NULL_USER = User.of(UUID.fromString("null"), "_");

    public static final Group MEMBER_GROUP = Group.Companion.get("Member");
    public static final @NotNull Server DEFAULT_SERVER = Server.of(ServerType.GLOBAL, 0, "The default server. An error has occurred in the code.");

    public static final @NotNull String JDBC_URL = "jdbc:mariadb://localhost:3306/rune_database";
    public static final @NotNull String USERNAME = "root"; //""sftpUser";
    public static final @NotNull String PASSWORD = "admin"; // ""B24Z69K11M.";
}
