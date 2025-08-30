package com.teamsimplyrs.prismaarcanum.system.spellsystem_deprecated.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Map;

import org.slf4j.Logger;

@Deprecated
public class SpellDataLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    public static final String SPELLS_DATA_DIRECTORY = "spells";

    public SpellDataLoader() {
        super(GSON, SPELLS_DATA_DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
//        SpellRegistryOld.clear();
//
//        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
//            try {
//                ResourceLocation path = entry.getKey();
//                JsonObject value = entry.getValue().getAsJsonObject();
//
//                SpellDataModel spellData = SpellFactory.parseSpell(value);
//                spellData.id = path;
//
//                SpellRegistryOld.register(path, spellData);
//                LOGGER.info("[Prisma Arcanum] Loaded spell JSON: {}", entry.getKey());
//            } catch (Exception e) {
//                System.err.println("[Prisma Arcanum] Failed to load spell JSON: " + entry.getKey() + " → " + e.getMessage());
//            }
//        }
//
//        NeoForge.EVENT_BUS.post(new SpellsReloadedEvent(SpellRegistryOld.getAllSpellData().size()));
    }
}
