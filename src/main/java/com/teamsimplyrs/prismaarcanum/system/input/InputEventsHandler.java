package com.teamsimplyrs.prismaarcanum.system.input;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.item.debug.DebugWand;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces.IMultiSpellHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT)
public class InputEventsHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        double scrollDelta = event.getScrollDeltaY();

        if (player.isShiftKeyDown() && player.getMainHandItem().getItem() instanceof IMultiSpellHolder spellHoldingItem) {
            float scrollDeltaX = (float)event.getScrollDeltaX();
            float scrollDeltaY = (float)event.getScrollDeltaY();

            spellHoldingItem.handleScrollCycling(scrollDeltaX, scrollDeltaY);

            event.setCanceled(true); // stop hotbar from scrolling
        }
    }

}
