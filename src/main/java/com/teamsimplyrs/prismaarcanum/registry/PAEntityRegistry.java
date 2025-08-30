package com.teamsimplyrs.prismaarcanum.registry;


import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.ManaPelletProjectile;
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
                        .sized(1f, 1f)
                        .build("mana_pellet_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
