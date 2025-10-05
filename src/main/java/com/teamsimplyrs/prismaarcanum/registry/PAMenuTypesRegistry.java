package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.client.menu.container.PrismaFocusBenchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PAMenuTypesRegistry {
    public static final DeferredRegister<MenuType<?>> PA_MENUS =
            DeferredRegister.create(Registries.MENU, PrismaArcanum.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PrismaFocusBenchMenu>> PRISMA_FOCUS_BENCH_MENU = registerMenuType("prisma_focus_bench_menu", PrismaFocusBenchMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return PA_MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus bus) {
        PA_MENUS.register(bus);
    }
}
