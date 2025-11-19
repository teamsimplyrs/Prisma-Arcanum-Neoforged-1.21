package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.block.PrismFocusBenchBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PABlockRegistry {
    public static final DeferredRegister.Blocks PA_BLOCKS = DeferredRegister.createBlocks(PrismaArcanum.MOD_ID);

    public static final DeferredBlock<Block> PRISM_FOCUS_BENCH = registerBlock(PrismFocusBenchBlock.name, () -> new PrismFocusBenchBlock(BlockBehaviour.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(10.0f)
            .sound(SoundType.GILDED_BLACKSTONE)
            .lightLevel(state -> 1)
            .noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = PA_BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        PAItemRegistry.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        PA_BLOCKS.register(eventBus);
    }

}
