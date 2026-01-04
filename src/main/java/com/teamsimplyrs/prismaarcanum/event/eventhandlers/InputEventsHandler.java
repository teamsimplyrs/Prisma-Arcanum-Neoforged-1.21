package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import java.util.List;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT)
public class InputEventsHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack hand = player.getMainHandItem();

        if (player.isShiftKeyDown() && hand.getItem() instanceof IMultiSpellHolder spellHoldingItem) {
            float scrollDeltaX = (float)event.getScrollDeltaX();
            float scrollDeltaY = (float)event.getScrollDeltaY();

            WandUtils.cycleSpellsOnScroll(hand, scrollDeltaX, scrollDeltaY);
            List<ResourceLocation> spellsBound = hand.get(PADataComponents.SPELLS_BOUND.get());
            Integer currentSpellIndex = hand.get(PADataComponents.CURRENT_SPELL_INDEX.get());
            if (spellsBound != null && !spellsBound.isEmpty()) {
                AbstractSpell spell = SpellRegistry.getSpell(spellsBound.get(currentSpellIndex == null ? 0 : currentSpellIndex));
                if (spell != null) {
                    player.sendSystemMessage(Component.literal(String.format("Current Spell: %s", spell.getDisplayName())));
                }
            }
            event.setCanceled(true); // stop hotbar from scrolling
        }
    }

}
