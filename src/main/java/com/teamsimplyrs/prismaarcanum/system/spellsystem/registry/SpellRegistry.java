package com.teamsimplyrs.prismaarcanum.system.spellsystem.registry;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {

    private static final Map<ResourceLocation, SpellDataModel> SPELLS = new HashMap<>();

    public static void loadAndRegisterAllSpells() {

    }

    public static void register(ResourceLocation path, SpellDataModel spellData) {
        SPELLS.put(path, spellData);
    }

    public static SpellDataModel getSpellData(ResourceLocation path) {
        return SPELLS.get(path);
    }

    public static Collection<SpellDataModel> getAllSpellData() {
        return SPELLS.values();
    }

    public static void clear() {
        SPELLS.clear();
    }
}
