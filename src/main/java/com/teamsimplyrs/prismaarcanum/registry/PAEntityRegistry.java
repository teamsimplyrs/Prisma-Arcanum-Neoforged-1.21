package com.teamsimplyrs.prismaarcanum.registry;


import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PAEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PrismaArcanum.MOD_ID);

    public static final Supplier<EntityType<ManaPelletProjectile>> MANA_PELLET_PROJECTILE =
        ENTITY_TYPES.register("mana_pellet_projectile",
                () -> EntityType.Builder.<ManaPelletProjectile>of(
                        ManaPelletProjectile::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .build("mana_pellet_projectile"));

    public static final Supplier<EntityType<FireballSpellProjectile>> FIREBALL_SPELL_PROJECTILE =
            ENTITY_TYPES.register("fireball_spell_projectile",
                    () -> EntityType.Builder.<FireballSpellProjectile>of(
                            FireballSpellProjectile::new, MobCategory.MISC)
                            .sized(0.75f, 0.75f)
                            .build("fireball_spell_projectile"));

    public static final Supplier<EntityType<NapalmShootBlankProjectile>> NAPALM_BLANK =
            ENTITY_TYPES.register("napalm_blank",
                    () -> EntityType.Builder.<NapalmShootBlankProjectile>of(
                            NapalmShootBlankProjectile::new, MobCategory.MISC)
                            .sized(0.2f,0.2f)
                            .build("napalm_blank"
                    ));

    public static final Supplier<EntityType<SpellEffectAreaEntity>> SPELL_EFFECT_AREA =
            ENTITY_TYPES.register("spell_effect_area",
                    () -> EntityType.Builder.<SpellEffectAreaEntity>of(SpellEffectAreaEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)                // default; actual size will sync via DATA_SIZE
                            .clientTrackingRange(64)          // important so clients see it
                            .updateInterval(2)                // how often to sync
                            .build("spell_effect_area"
                    ));

    public static final Supplier<EntityType<WindPoolBlankProjectile>> WINDPOOL_BLANK =
            ENTITY_TYPES.register("windpool_blank",
                    () -> EntityType.Builder.<WindPoolBlankProjectile>of(WindPoolBlankProjectile::new, MobCategory.MISC)
                            .sized(1.0f,1.0f)
                            .clientTrackingRange(64)
                            .updateInterval(2)
                            .build("windpool_blank"
                    ));

    public static final Supplier<EntityType<BounceZapProjectile>> BOUNCEZAP_PROJECTILE =
            ENTITY_TYPES.register("bouncezap_projectile",
                    () -> EntityType.Builder.<BounceZapProjectile>of(
                                    BounceZapProjectile::new, MobCategory.MISC)
                            .sized(0.75f, 0.75f)
                            .build("bouncezap_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
