package com.teamsimplyrs.prismaarcanum.block;

import com.mojang.serialization.MapCodec;
import com.teamsimplyrs.prismaarcanum.block.blockentity.PrismaFocusBenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PrismaFocusBenchBlock extends BaseEntityBlock {
    public static final String name = "prisma_focus_bench";
    public static final MapCodec<PrismaFocusBenchBlock> CODEC = simpleCodec(PrismaFocusBenchBlock::new);

    public PrismaFocusBenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PrismaFocusBenchBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock() && level.getBlockEntity(pos) instanceof PrismaFocusBenchBlockEntity prismaFocusBenchBE) {
            prismaFocusBenchBE.dropContents();
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
