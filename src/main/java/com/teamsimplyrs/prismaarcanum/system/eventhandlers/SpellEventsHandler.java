package com.teamsimplyrs.prismaarcanum.system.eventhandlers;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.item.debug.DebugWand;
import com.teamsimplyrs.prismaarcanum.registry.PAItemRegistry;
import com.teamsimplyrs.prismaarcanum.system.events.SpellsReloadedEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class SpellEventsHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onSpellsReloaded(SpellsReloadedEvent event) {
        LOGGER.info("[PrismaArcanum] Spells reloaded. Total spells: {}", event.getCount());
        ((DebugWand)PAItemRegistry.DEBUG_WAND.get()).onSpellsReloaded();
    }
}
