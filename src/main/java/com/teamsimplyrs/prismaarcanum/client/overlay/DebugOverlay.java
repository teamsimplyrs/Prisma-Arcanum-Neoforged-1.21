package com.teamsimplyrs.prismaarcanum.client.overlay;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class DebugOverlay {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiLayerEvent.Post event) {

    }
}
