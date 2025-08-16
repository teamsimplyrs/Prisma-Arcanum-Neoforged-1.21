package com.teamsimplyrs.prismaarcanum.system.spellsystem.registry;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

import static java.util.Collections.unmodifiableSet;

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

    public static Set<Map.Entry<ResourceLocation, SpellDataModel>> getAllEntries() {
        return unmodifiableSet(SPELLS.entrySet());
    }

    public static void clear() {
        SPELLS.clear();
    }
}
