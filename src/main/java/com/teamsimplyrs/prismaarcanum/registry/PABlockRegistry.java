package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.block.PrismaFocusBenchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PABlockRegistry {
    public static final DeferredRegister.Blocks PA_BLOCKS = DeferredRegister.createBlocks(PrismaArcanum.MOD_ID);

    public static final DeferredBlock<Block> PRISMA_FOCUS_BENCH = PA_BLOCKS.register(PrismaFocusBenchBlock.name, () -> new PrismaFocusBenchBlock(BlockBehaviour.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(10.0f)
            .sound(SoundType.GILDED_BLACKSTONE)
            .lightLevel(state -> 7)
            .noOcclusion()));
}
