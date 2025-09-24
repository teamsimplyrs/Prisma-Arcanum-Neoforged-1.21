package com.teamsimplyrs.prismaarcanum.block.blockentity;

import com.mojang.serialization.MapCodec;
import com.teamsimplyrs.prismaarcanum.registry.PABlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PrismaFocusBenchBlockEntity extends BlockEntity {
    public static final String name = "prisma_focus_bench_be";
    public final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };


    public PrismaFocusBenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(PABlockEntityRegistry.PRISMA_FOCUS_BENCH.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.loadAdditional(tag, registries);
    }
}
