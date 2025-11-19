package com.teamsimplyrs.prismaarcanum.client.overlay;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.casting.ClientCooldownManager;
import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.GuiUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ChromanaHUDOverlayRenderer {


    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;
        if (mc.options.hideGui || player == null) {
            return;
        }

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        PlayerChromana manaData = player.getData(PADataAttachmentsRegistry.CHROMANA.get());
        var cooldowns = ClientCooldownManager.get();

        ItemStack handItem = player.getMainHandItem();

        Component manaDisplay = Component.literal(String.format("Chromana: %d / %d", manaData.getCurrent(), manaData.getMax()));
        Component cooldownDisplay = null;

        int manaX = 10;
        int manaY = height - 10;
        int cooldownX = 10;
        int cooldownY = height - 20;

        int color;
        AbstractSpell spell = null;
        ResourceLocation currentSpellRL = null;

        if (handItem.getItem() instanceof AbstractCastable) {
          currentSpellRL = WandUtils.getCurrentSpell(handItem);
          if (currentSpellRL != null) {
              spell = SpellRegistry.getSpell(currentSpellRL);

              if (cooldowns.isOnCooldown(currentSpellRL)) {
                  cooldownDisplay = Component.literal(String.format("%s Cooldown: %.2f", spell.getDisplayName(), cooldowns.getCooldownSeconds(currentSpellRL)));
              }
          }

            color = spell != null ? GuiUtils.getTextColorForElement(spell.getElement()) : 0xFFFFFF;
            event.getGuiGraphics().drawString(mc.font, manaDisplay, manaX, manaY, color, true);
            if (cooldownDisplay != null) {
                event.getGuiGraphics().drawString(mc.font, cooldownDisplay, cooldownX, cooldownY, color, true);
            }
        }
    }
}
