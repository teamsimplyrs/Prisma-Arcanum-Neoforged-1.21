package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.event.SpellsLoadedEvent;
import com.teamsimplyrs.prismaarcanum.item.debug.DebugWand;
import com.teamsimplyrs.prismaarcanum.registry.PAItemRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class SpellEventsHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onSpellsLoaded(SpellsLoadedEvent event) {
        DebugWand debugWand = (DebugWand)PAItemRegistry.DEBUG_WAND.get();
        debugWand.loadSpells();
    }
}
