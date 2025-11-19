package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.block.blockentity.PrismFocusBenchBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PABlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> PA_BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PrismaArcanum.MOD_ID);

    public static final Supplier<BlockEntityType<PrismFocusBenchBlockEntity>> PRISMA_FOCUS_BENCH = PA_BLOCK_ENTITIES.register(
            PrismFocusBenchBlockEntity.name,
            () -> BlockEntityType.Builder.of(
                    PrismFocusBenchBlockEntity::new,
                    PABlockRegistry.PRISM_FOCUS_BENCH.get()
            ).build(null));


    public static void register(IEventBus eventBus) {
        PA_BLOCK_ENTITIES.register(eventBus);
    }
}
