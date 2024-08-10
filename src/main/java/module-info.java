module com.dank1234.utils {
    requires org.bukkit;
    requires api;
    requires org.jetbrains.annotations;
    requires java.sql;
    requires jsr305;
    requires reflections;
    requires org.yaml.snakeyaml;

    exports com.dank1234.utils.players;
    exports com.dank1234.utils.data;
    exports com.dank1234.utils.command;
    exports com.dank1234.utils.server;
    exports com.dank1234.utils.gui;
    exports com.dank1234.utils.wrapper.message;
    exports com.dank1234.utils.wrapper.player;

    // version 0.0.1;
}