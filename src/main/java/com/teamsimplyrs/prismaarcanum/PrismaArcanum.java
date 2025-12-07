package com.teamsimplyrs.prismaarcanum;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.animation.FirstPersonTorsoModifier;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.client.menu.screen.PrismFocusBenchScreen;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.entity.client.GenericEmptyRenderer;
import com.teamsimplyrs.prismaarcanum.entity.client.NapalmBlankRenderer;
import com.teamsimplyrs.prismaarcanum.entity.client.SpellEffectAreaRenderer;
import com.teamsimplyrs.prismaarcanum.entity.client.monster.RippleSeekerRenderer;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.FireballSpellProjectileRenderer;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.ManaPelletRenderer;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.RippleSeekerProjectileRenderer;
import com.teamsimplyrs.prismaarcanum.registry.*;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

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

        // Call all registries here
        SpellRegistry.register(modEventBus);
        PABlockRegistry.register(modEventBus);
        PAItemRegistry.register(modEventBus);
        PAEntityRegistry.register(modEventBus);
        PABlockEntityRegistry.register(modEventBus);
        PAMenuTypesRegistry.register(modEventBus);
        PADataComponents.register(modEventBus);
        PACreativeTabsRegistry.register(modEventBus);
        PADataAttachmentsRegistry.register(modEventBus);
        PASpellEffectRegistry.register(modEventBus);

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
            LOGGER.info("Client Setup");
            EntityRenderers.register(PAEntityRegistry.MANA_PELLET_PROJECTILE.get(), ManaPelletRenderer::new);
            EntityRenderers.register(PAEntityRegistry.FIREBALL_SPELL_PROJECTILE.get(), FireballSpellProjectileRenderer::new);
            EntityRenderers.register(PAEntityRegistry.NAPALM_BLANK.get(), NapalmBlankRenderer::new);
            EntityRenderers.register(PAEntityRegistry.SPELL_EFFECT_AREA.get(), SpellEffectAreaRenderer::new);
            EntityRenderers.register(PAEntityRegistry.WINDPOOL_BLANK.get(), ctx -> new GenericEmptyRenderer<>(ctx));
            EntityRenderers.register(PAEntityRegistry.BOUNCEZAP_PROJECTILE.get(), ctx -> new GenericEmptyRenderer<>(ctx));
            EntityRenderers.register(PAEntityRegistry.RIPPLE_SEEKER.get(), RippleSeekerRenderer::new);
            EntityRenderers.register(PAEntityRegistry.RIPPLE_SEEKER_PROJECTILE.get(), RippleSeekerProjectileRenderer::new);
            EntityRenderers.register(PAEntityRegistry.SPRING_DEATH_TRACKER.get(),  ctx -> new GenericEmptyRenderer<>(ctx));

            //todo move this somewhere else
            event.enqueueWork(() -> {
                PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ResourceLocation.fromNamespaceAndPath(MOD_ID,"spell_caster"),1000,
                        player -> new PlayerAnimationController(player,(controller,state,animSetter)-> PlayState.STOP).addModifierBefore(new FirstPersonTorsoModifier()));
            });

        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(PAMenuTypesRegistry.PRISMA_FOCUS_BENCH_MENU.get(), PrismFocusBenchScreen::new);
        }
    }
}
