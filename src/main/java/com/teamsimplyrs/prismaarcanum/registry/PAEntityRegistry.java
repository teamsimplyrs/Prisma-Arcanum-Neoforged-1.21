package com.teamsimplyrs.prismaarcanum.registry;


import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.RippleSeekerProjectile;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.FireballSpellProjectile;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.ManaPelletProjectile;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PAEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PrismaArcanum.MOD_ID);

    /* ===== PROJECTILES ===== */

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

    public static final Supplier<EntityType<RippleSeekerProjectile>> RIPPLE_SEEKER_PROJECTILE =
            ENTITY_TYPES.register("ripple_seeker_projectile",
                    () -> EntityType.Builder.<RippleSeekerProjectile>of(
                            RippleSeekerProjectile::new, MobCategory.MISC)
                            .sized(0.75f, 0.75f)
                            .build("ripple_seeker_projectile"));


    /* ===== MONSTERS ===== */

    public static final Supplier<EntityType<RippleSeekerEntity>> RIPPLE_SEEKER =
            ENTITY_TYPES.register("ripple_seeker",
                    () -> EntityType.Builder.<RippleSeekerEntity>of(
                            RippleSeekerEntity::new, MobCategory.MONSTER)
                            .sized(1f, 1f)
                            .build("ripple_seeker"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
