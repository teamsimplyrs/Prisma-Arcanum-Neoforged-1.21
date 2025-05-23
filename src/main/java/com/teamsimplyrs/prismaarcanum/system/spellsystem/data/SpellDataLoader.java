package com.teamsimplyrs.prismaarcanum.system.spellsystem.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.factory.SpellFactory;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Map;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.registry.SpellRegistry;
import org.slf4j.Logger;

public class SpellDataLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    public static final String SPELLS_DATA_DIRECTORY = "spells";

    public SpellDataLoader() {
        super(GSON, SPELLS_DATA_DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        SpellRegistry.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            try {
                ResourceLocation path = entry.getKey();
                JsonObject value = entry.getValue().getAsJsonObject();

                SpellDataModel spellData = SpellFactory.parseSpell(value);
                SpellRegistry.register(path, spellData);

                LOGGER.info("[Prisma Arcanum] Loaded spell JSON: {}", entry.getKey());
            } catch (Exception e) {
                System.err.println("[Prisma Arcanum] Failed to load spell JSON: " + entry.getKey() + " → " + e.getMessage());
            }
        }
    }
}
