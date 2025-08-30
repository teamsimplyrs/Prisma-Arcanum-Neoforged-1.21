package com.teamsimplyrs.prismaarcanum;

import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.entity.client.ManaPelletRenderer;
import com.teamsimplyrs.prismaarcanum.entity.custom.ManaPelletProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PACreativeTabsRegistry;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.registry.PAItemRegistry;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.system.spellsystem_deprecated.data.SpellDataLoader;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PrismaArcanum.MOD_ID)
public class PrismaArcanum
{
    public static final String MOD_ID = "prismaarcanum";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PrismaArcanum(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // NeoForge specific Events
        NeoForge.EVENT_BUS.addListener(this::registerReloadListeners);

        // Note that this is necessary if and only if we want *this* class (PrismaArcanum) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Call all registers here
        SpellRegistry.register(modEventBus);
        PACreativeTabsRegistry.register(modEventBus);
        PAItemRegistry.register(modEventBus);
        PAEntityRegistry.register(modEventBus);
        PADataComponents.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerReloadListeners(AddReloadListenerEvent event) {

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(PAItemRegistry.DEBUG_WAND);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(PAEntityRegistry.MANA_PELLET_PROJECTILE.get(), ManaPelletRenderer::new);
        }
    }
}
