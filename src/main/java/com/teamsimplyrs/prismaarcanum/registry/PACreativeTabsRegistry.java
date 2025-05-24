package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PACreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrismaArcanum.MOD_ID);

    public static final Supplier<CreativeModeTab> PA_PRISMATIC_TOOLS_TAB = CREATIVE_TAB_REGISTRY.register("prismatic_tools_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PAItemRegistry.DEBUG_WAND.get()))
            .title(Component.translatable("creativetab.prismaarcanum.prismatic_tools"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(PAItemRegistry.DEBUG_WAND);
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TAB_REGISTRY.register(eventBus);
    }
}
