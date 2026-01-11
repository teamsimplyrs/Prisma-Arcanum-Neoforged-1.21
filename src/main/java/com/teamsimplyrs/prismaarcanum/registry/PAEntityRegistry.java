package com.teamsimplyrs.prismaarcanum.registry;


import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.IgniumLegionnaireEntity;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.*;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import com.teamsimplyrs.prismaarcanum.entity.custom.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PAEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PrismaArcanum.MOD_ID);

    /** ===== PROJECTILES ===== */

    public static final Supplier<EntityType<ManaPelletProjectile>> MANA_PELLET_PROJECTILE =
        ENTITY_TYPES.register("mana_pellet_projectile",
                () -> EntityType.Builder.<ManaPelletProjectile>of(
                        ManaPelletProjectile::new, MobCategory.MISC)
                        .sized(0.5f, 0.5f)
                        .build("mana_pellet_projectile"));

    public static final Supplier<EntityType<OmenSliceProjectile>> OMEN_SLICE_PROJECTILE =
            ENTITY_TYPES.register("omen_slice_projectile",
                    () -> EntityType.Builder.<OmenSliceProjectile>of(
                                    OmenSliceProjectile::new, MobCategory.MISC)
                            .sized(1f, 0.5f)
                            .build("omen_slice_projectile"));

    public static final Supplier<EntityType<FireballSpellProjectile>> FIREBALL_SPELL_PROJECTILE =
            ENTITY_TYPES.register("fireball_spell_projectile",
                    () -> EntityType.Builder.<FireballSpellProjectile>of(
                            FireballSpellProjectile::new, MobCategory.MISC)
                            .sized(0.75f, 0.75f)
                            .build("fireball_spell_projectile"));

    public static final Supplier<EntityType<NapalmSprayProjectile>> NAPALM_SPRAY_PROJECTILE =
            ENTITY_TYPES.register("napalm_blank",
                    () -> EntityType.Builder.<NapalmSprayProjectile>of(
                            NapalmSprayProjectile::new, MobCategory.MISC)
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

    public static final Supplier<EntityType<VortexTrapProjectile>> VORTEX_TRAP_PROJECTILE =
            ENTITY_TYPES.register("vortex_trap_projectile",
                    () -> EntityType.Builder.<VortexTrapProjectile>of(VortexTrapProjectile::new, MobCategory.MISC)
                            .sized(1.0f,1.0f)
                            .clientTrackingRange(64)
                            .updateInterval(2)
                            .build("vortex_trap_projectile"
                    ));

    public static final Supplier<EntityType<ArcOrbProjectile>> ARC_ORB_PROJECTILE =
            ENTITY_TYPES.register("arc_orb_projectile",
                    () -> EntityType.Builder.<ArcOrbProjectile>of(
                                    ArcOrbProjectile::new, MobCategory.MISC)
                            .sized(0.75f, 0.75f)
                            .build("arc_orb_projectile"));

    public static final Supplier<EntityType<RippleSeekerProjectile>> RIPPLE_SEEKER_PROJECTILE =
            ENTITY_TYPES.register("ripple_seeker_projectile",
                    () -> EntityType.Builder.<RippleSeekerProjectile>of(
                            RippleSeekerProjectile::new, MobCategory.MISC)
                            .sized(0.75f, 0.75f)
                            .build("ripple_seeker_projectile"));



    /** ==== MISC UTILS ==== */

    public static final Supplier<EntityType<SpringDeathTrackerEntity>> SPRING_DEATH_TRACKER =
            ENTITY_TYPES.register("spring_death_tracker",
                    () -> EntityType.Builder.<SpringDeathTrackerEntity>of(
                                    SpringDeathTrackerEntity::new, MobCategory.MISC)
                            .sized(0.1f,0.1f)
                            .build("spring_death_tracker"));

    /** ===== MONSTERS ===== */

    public static final Supplier<EntityType<RippleSeekerEntity>> RIPPLE_SEEKER =
            ENTITY_TYPES.register("ripple_seeker",
                    () -> EntityType.Builder.<RippleSeekerEntity>of(
                            RippleSeekerEntity::new, MobCategory.MONSTER)
                            .sized(1f, 1f)
                            .build("ripple_seeker")
            );

    public static final Supplier<EntityType<IgniumLegionnaireEntity>> IGNIUM_LEGIONNAIRE =
            ENTITY_TYPES.register("ignium_legionnaire",
                    () -> EntityType.Builder.<IgniumLegionnaireEntity>of(
                            IgniumLegionnaireEntity::new, MobCategory.MONSTER)
                            .sized(1.25f, 2.75f)
                            .build("ignium_legionnaire")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
