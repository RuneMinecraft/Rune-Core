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
}
