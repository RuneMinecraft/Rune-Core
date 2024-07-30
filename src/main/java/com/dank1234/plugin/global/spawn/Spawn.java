package com.dank1234.plugin.global.spawn;

import com.dank1234.plugin.Main;
import com.dank1234.utils.data.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class Spawn {
    private final Config config = Main.get().config();;

    private World world;
    private String worldName;
    private final int x, y, z;
    private double xD, yD, zD;
    private double yD1, pD;
    private final float yaw;
    private final float pitch;

    private Spawn() {
        if (Main.get().isEnabled()) {
            worldName = config.getValue("spawn.world");
            world = Bukkit.getWorld(worldName);

            xD = (double) config.getObjectValue("spawn.x");
            yD = (double) config.getObjectValue("spawn.y");
            zD = (double) config.getObjectValue("spawn.z");

            yD1 = (double) config.getObjectValue("spawn.yaw");
            pD = (double) config.getObjectValue("spawn.pitch");
        }
        yaw = (float) yD1;
        pitch = (float) pD;

        x = (int) xD;
        y = (int) yD;
        z = (int) zD;
    }
    public static Spawn get() {
        return new Spawn();
    }

    public World world() {
        return world;
    }
    public String worldName() {
        return worldName;
    }

    public int x() {
        return x;
    }
    public int y() {
        return y;
    }
    public int z() {
        return z;
    }

    public float yaw() {
        return yaw;
    }
    public float pitch() {
        return pitch;
    }

    public Location location() {
        return new Location(world, xD, yD, zD, yaw, pitch);
    }

    @Override
    public String toString() {
        return this.location().toString();
    }
}
