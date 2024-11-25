package com.dank1234.utils;

import com.dank1234.utils.server.Server;
import com.dank1234.utils.server.ServerType;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Consts {
    public static final @NotNull Group MEMBER_GROUP = Objects.requireNonNull(LuckPermsProvider.get().getGroupManager().getGroup("default"));
    public static final @NotNull Server DEFAULT_SERVER = Server.of(ServerType.GLOBAL, 0, "The default server. An error has occurred in the code.");

    public static final @NotNull String JDBC_URL = "jdbc:mariadb://localhost:3306/minecraft_database";
    public static final @NotNull String USERNAME = "sftpUser";
    public static final @NotNull String PASSWORD = "B24Z69K11M.";
}
