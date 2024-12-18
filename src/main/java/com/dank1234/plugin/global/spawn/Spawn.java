package com.dank1234.plugin.global.spawn;

import com.dank1234.api.wrapper.location.Location;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Spawn extends Location {
    public Spawn(
            @NotNull World world,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {
        super(world, x, y, z, yaw, pitch);
    }

    public static Spawn get() {
        return new Spawn(
                Objects.requireNonNull(Bukkit.getWorld("box1")),
                0, 100, 0,
                0, 0
        );
    }
}
