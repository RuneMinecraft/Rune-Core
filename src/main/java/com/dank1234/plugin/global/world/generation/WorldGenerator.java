package com.dank1234.plugin.global.world.generation;

import com.dank1234.plugin.global.world.generation.generator.VoidWorldGenerator;
import com.dank1234.utils.Logger;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldGenerator {
    private final String name;
    private final WorldGenerationType type;

    private WorldGenerator(String name, WorldGenerationType type) {
        this.name = name;
        this.type = type;
    }

    public static World create(String name, WorldGenerationType type) {
        WorldCreator creator = new WorldCreator(name);

        switch (type) {
            case VOID -> creator.generator(VoidWorldGenerator.get());
            case NORMAL -> creator.generator(VoidWorldGenerator.get()); //TODO: WRITE THIS CLASS
            case NETHER -> creator.generator(VoidWorldGenerator.get()); //TODO: WRITE THIS CLASS
            case END -> creator.generator(VoidWorldGenerator.get()); //TODO: WRITE THIS CLASS
            default -> Logger.infoRaw("");
        };

        return creator.createWorld();
    }

    public String name() {
        return this.name;
    }
    public WorldGenerationType type() {
        return this.type;
    }
}
