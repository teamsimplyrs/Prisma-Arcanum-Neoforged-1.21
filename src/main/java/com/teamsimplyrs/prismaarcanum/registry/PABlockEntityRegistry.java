package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.block.blockentity.PrismaFocusBenchBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PABlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> PA_BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PrismaArcanum.MOD_ID);

    public static final Supplier<BlockEntityType<PrismaFocusBenchBlockEntity>> PRISMA_FOCUS_BENCH = PA_BLOCK_ENTITIES.register(
            PrismaFocusBenchBlockEntity.name,
            () -> BlockEntityType.Builder.of(
                    PrismaFocusBenchBlockEntity::new,
                    PABlockRegistry.PRISMA_FOCUS_BENCH.get()
            ).build(null));


    public static void register(IEventBus eventBus) {
        PA_BLOCK_ENTITIES.register(eventBus);
    }
}
