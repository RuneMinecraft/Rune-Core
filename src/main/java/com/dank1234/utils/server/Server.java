package com.dank1234.utils.server;

import com.dank1234.plugin.Main;
import com.dank1234.utils.Consts;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@NotNull
public final class Server {
    private final ServerType TYPE;
    private final Integer RELEASE;
    private final String DESCRIPTION;

    private final String SERVER_NAME;

    private Server(final ServerType type, final int release, final String description) {
        this.TYPE = type;
        this.RELEASE = release;
        this.DESCRIPTION = description;

        this.SERVER_NAME = this.TYPE.name()+(release < 10 ? "0" + this.RELEASE : this.RELEASE);
    }
    public static Server of(final ServerType type, final int release, final String description) {
        return new Server(type, release, description);
    }
    public static Server of() {
        try {
            final ServerType type = ServerType.valueOf(Main.get().config().getValue("server.type"));
            final int release = Integer.parseInt(Objects.requireNonNull(Main.get().config().getValue("server.release")));
            final String description = Main.get().config().getValue("server.description");
            return new Server(type, release, description);
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
    public String DESCRIPTION() {
        return this.DESCRIPTION;
    }
    public String SERVER_NAME() {
        return this.SERVER_NAME;
    }

    @Override
    public String toString() {
        return "Server[TYPE="+this.TYPE()+", RELEASE="+this.RELEASE()+"]";
    }
}
