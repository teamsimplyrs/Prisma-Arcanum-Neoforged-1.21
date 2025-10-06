package com.teamsimplyrs.prismaarcanum.api.spell.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.event.SpellsLoadedEvent;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.mentis.ManaPellet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.function.Supplier;

public class SpellRegistry {
    public static final ResourceKey<Registry<AbstractSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "spells"));
    public static final Registry<AbstractSpell> SPELL_REGISTRY = new RegistryBuilder<>(SPELL_REGISTRY_KEY).sync(true).create();

    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, PrismaArcanum.MOD_ID);

    public static final Supplier<ManaPellet> MANA_PELLET = SPELLS.register(ManaPellet.spellID, ManaPellet::new);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
        NeoForge.EVENT_BUS.post(new SpellsLoadedEvent(getAllSpells().size()));
    }

    public static List<AbstractSpell> getAllSpells() {
        return SPELL_REGISTRY.stream().toList();
    }

    public static AbstractSpell getSpell(ResourceLocation id) {
        return SPELL_REGISTRY.get(id);
    }
}
