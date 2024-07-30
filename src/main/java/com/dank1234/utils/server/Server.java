package com.dank1234.utils.server;

import com.dank1234.plugin.Main;
import com.dank1234.utils.Consts;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@NotNull
public final class Server {
    private final ServerType TYPE;
    private final Integer RELEASE;

    private final String SERVER_NAME;

    private Server(final ServerType type, final int release) {
        this.TYPE = type;
        this.RELEASE = release;

        this.SERVER_NAME = this.TYPE.name()+(release < 10 ? "0" + this.RELEASE : this.RELEASE);
    }
    public static Server of(final ServerType type, final int release) {
        return new Server(type, release);
    }
    public static Server of() {
        try {
            final ServerType type = ServerType.valueOf(Main.get().config().getValue("server.type"));
            final int release = Integer.parseInt(Objects.requireNonNull(Main.get().config().getValue("server.release")));
            return new Server(type, release);
        }catch(NullPointerException e) {
            e.printStackTrace();
            return Consts.DEFAULT_SERVER;
        }
    }

    public ServerType TYPE() {
        return this.TYPE;
    }
    public Integer RELEASE() {
        return this.RELEASE;
    }
    public String SERVER_NAME() {
        return this.SERVER_NAME;
    }

    @Override
    public String toString() {
        return "Server[TYPE="+this.TYPE()+", RELEASE="+this.RELEASE()+"]";
    }
}
