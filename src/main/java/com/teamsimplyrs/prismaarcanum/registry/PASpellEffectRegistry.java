package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.ignis.NapalmBurnEffect;
import com.teamsimplyrs.prismaarcanum.api.status_effect.fulgur.ZappedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PASpellEffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, PrismaArcanum.MOD_ID);

    public static final Holder<MobEffect> NAPALM_BURN =
            EFFECTS.register("napalm_burn", NapalmBurnEffect::new);

    public static final Holder<MobEffect> ZAPPED = 
            EFFECTS.register("zapped", ZappedEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
