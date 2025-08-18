package com.teamsimplyrs.prismaarcanum.system.castingsystem;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CastingEngineManager {
    private static final List<CastingEngine> ACTIVE_ENGINES = new ArrayList<>();

    public static void startEngine(CastingEngine engine) {
        ACTIVE_ENGINES.add(engine);
    }

    public static void tickAll(MinecraftServer server) {
        Iterator<CastingEngine> it = ACTIVE_ENGINES.iterator();
        while (it.hasNext()) {
            CastingEngine engine = it.next();

            // For now, just tick against the original world/player
            engine.tick();

            if (engine.isCastingComplete()) {
                it.remove();
            }
        }
    }
}
