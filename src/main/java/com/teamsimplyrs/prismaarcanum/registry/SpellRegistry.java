package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.spells.aqua.SpringOfDeath;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.spells.fulgur.ArcOrb;
import com.teamsimplyrs.prismaarcanum.spells.ignis.FireballSpell;
import com.teamsimplyrs.prismaarcanum.spells.ignis.NapalmSpraySpell;
import com.teamsimplyrs.prismaarcanum.spells.mentis.OmenSlice;
import com.teamsimplyrs.prismaarcanum.spells.mentis.MagicBullet;
import com.teamsimplyrs.prismaarcanum.spells.mentis.ManaPellet;
import com.teamsimplyrs.prismaarcanum.spells.mentis.MysticMissile;
import com.teamsimplyrs.prismaarcanum.spells.ventus.TailwindSpell;
import com.teamsimplyrs.prismaarcanum.spells.ventus.VortexTrap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpellRegistry {
    public static final ResourceKey<Registry<AbstractSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "spells"));
    public static final Registry<AbstractSpell> SPELL_REGISTRY = new RegistryBuilder<>(SPELL_REGISTRY_KEY).sync(true).create();

    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY, PrismaArcanum.MOD_ID);

    /// ==== All spells go here for registration ====

    /// ==Mentis Spells==
    public static final Supplier<ManaPellet> MANA_PELLET = SPELLS.register(ManaPellet.spellID, ManaPellet::new);
    public static final Supplier<MagicBullet> MAGIC_BULLET = SPELLS.register(MagicBullet.spellID, MagicBullet::new);
    public static final Supplier<MysticMissile> MYSTIC_MISSILE = SPELLS.register(MysticMissile.spellID, MysticMissile::new);
    public static final Supplier<OmenSlice> INTENT_SCAR = SPELLS.register(OmenSlice.spellID, OmenSlice::new);

    /// == Ignis Spells ==
    public static final Supplier<FireballSpell> FIREBALL = SPELLS.register(FireballSpell.spellID, FireballSpell::new);
    public static final Supplier<NapalmSpraySpell> NAPALM_SPRAY = SPELLS.register(NapalmSpraySpell.spellID, NapalmSpraySpell::new);

    ///  == Aqua Spells ==
    public static final Supplier<SpringOfDeath> SPRING_OF_DEATH = SPELLS.register(SpringOfDeath.spellID, SpringOfDeath::new);

    /// == Ventus Spells ==
    public static final Supplier<VortexTrap> WINDPOOL = SPELLS.register(VortexTrap.spellID, VortexTrap::new);
    public static final Supplier<TailwindSpell> TAILWIND = SPELLS.register(TailwindSpell.spellID, TailwindSpell::new);

    ///  == Fulgur Spells ==
    public static final Supplier<ArcOrb> BOUNCEZAP = SPELLS.register(ArcOrb.spellID, ArcOrb::new);

    // ==============================================

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
//        NeoForge.EVENT_BUS.post(new SpellsLoadedEvent(getAllSpells().size()));
    }

    public static List<AbstractSpell> getAllSpells() {
        return SPELL_REGISTRY.stream().toList();
    }

    public static List<ResourceLocation> getAllSpellIDs() {
        return SPELL_REGISTRY.keySet().stream().toList();
    }

    public static AbstractSpell getSpell(ResourceLocation id) {
        return SPELL_REGISTRY.get(id);
    }

    public static List<AbstractSpell> getSpells(List<ResourceLocation> spellIDs) {
        if (spellIDs == null || spellIDs.isEmpty()) {
            return null;
        }

        List<AbstractSpell> result = new ArrayList<>();
        for (var id: spellIDs) {
            result.add(getSpell(id));
        }

        return result;
    }
}
