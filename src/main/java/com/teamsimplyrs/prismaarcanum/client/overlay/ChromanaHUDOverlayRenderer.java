package com.teamsimplyrs.prismaarcanum.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.GuiUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ChromanaHUDOverlayRenderer {

    private static final ResourceLocation MANA_BAR = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/hud/mana_bar.png");
    private static final ResourceLocation MANA_FILL = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/hud/mana_fill.png");
    private static final ResourceLocation NAMEPLATE = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/hud/nameplate.png");
    private static final ResourceLocation NAMEPLATE_CHARGE_FILL = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/hud/nameplate_charge_fill.png");
    private static final ResourceLocation SPELL_SLOT_ACTIVE = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/hud/spell_slot_active.png");
    private static final ResourceLocation SPELL_SLOT_INACTIVE = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/hud/spell_slot_inactive.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (mc.options.hideGui || player == null) {
            return;
        }

        ItemStack handItem = player.getMainHandItem();

        if (!(handItem.getItem() instanceof AbstractCastable castable)) {
            return;
        }

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        var cooldowns = player.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());
        PlayerChromana manaData = player.getData(PADataAttachmentsRegistry.CHROMANA.get());
        GuiGraphics gg = event.getGuiGraphics();
        Component cooldownDisplay = null;

        int cooldownX = 10;
        int cooldownY = height - 20;

        int color;
        AbstractSpell spell = null;
        ResourceLocation currentSpellID = WandUtils.getCurrentSpellID(handItem);
        ResourceLocation nextSpellID = WandUtils.getNextSpellID(handItem);
        ResourceLocation previousSpellID = WandUtils.getPreviousSpellID(handItem);

        if (currentSpellID != null) {
            spell = SpellRegistry.getSpell(currentSpellID);

            if (cooldowns.isOnCooldown(currentSpellID)) {
                cooldownDisplay = Component.literal(String.format("%s Cooldown: %.2f", spell.getDisplayName(), cooldowns.getCooldownSeconds(currentSpellID)));
            }
        }

        color = spell != null ? GuiUtils.getTextColorForElement(spell.getElement()) : 0xFFFFFF;
        if (cooldownDisplay != null) {
            gg.drawString(mc.font, cooldownDisplay, cooldownX, cooldownY, color, true);
        }

        float normalizedMana = (float)manaData.getCurrent() / (float)manaData.getMax();

        float scaleFactor = 1.5f;
        int hudYoffset = height - 10;

        // === TINTED ===
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        int fillBarHeight = Mth.floor(normalizedMana * 48);

        gg.setColor(r, g, b, 1f);
        gg.blit(MANA_FILL, 4, hudYoffset - 24 - fillBarHeight, 0, 48 - fillBarHeight, 48, fillBarHeight, 48, 48);


        // === RESET TINT ===
        gg.setColor(1f, 1f, 1f, 1f);

        // === UNTINTED ===
        gg.blit(MANA_BAR, 4, hudYoffset - 72, 0, 0, 48, 48, 48, 48);
        gg.blit(NAMEPLATE, 70, hudYoffset - 68, 0, 40, 128, 40, 128, 40); // TO DO: make a smaller version of the nameplate that renders in priority; longer version renders only if spell name is long enough (try to avoid very long names)
        gg.blit(SPELL_SLOT_ACTIVE, 38, hudYoffset - 72, 0, 48, 48, 48, 48, 48);
        gg.blit(SPELL_SLOT_INACTIVE, 52, hudYoffset - 110, 0, 56, 56, 56, 56, 56);
        gg.blit(SPELL_SLOT_INACTIVE, 52, hudYoffset - 42, 0, 56, 56, 56, 56, 56);

        Component manaDisplay = Component.literal(String.format("%d", manaData.getCurrent()));
        gg.drawString(mc.font, manaDisplay, 26, hudYoffset - 82, color, true);

//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
        if (spell != null) {
            Component spellNameDisplay = Component.literal(String.format(spell.getDisplayName()));
            gg.drawString(mc.font, spellNameDisplay, 90, hudYoffset - 52, color, true);

            ResourceLocation spellIcon = GuiUtils.getSpellIcon(currentSpellID);
            gg.blit(spellIcon, 50, hudYoffset - 60, 0, 24, 24, 24, 24, 24);
        }

        if (nextSpellID != null) {
            ResourceLocation spellIcon = GuiUtils.getSpellIcon(nextSpellID);
            gg.blit(spellIcon, 74, hudYoffset - 88, 0, 12, 12, 12, 12, 12);
        }

        if (previousSpellID != null) {
            ResourceLocation spellIcon = GuiUtils.getSpellIcon(previousSpellID);
            gg.blit(spellIcon, 74, hudYoffset - 20, 0, 12, 12, 12, 12, 12);
        }

//        RenderSystem.disableBlend();
    }
}
