package com.teamsimplyrs.prismaarcanum.client.menu.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.client.menu.container.PrismFocusBenchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PrismFocusBenchScreen extends AbstractContainerScreen<PrismFocusBenchMenu> {

    private static final ResourceLocation GUI_TEX = ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/gui/prism_focus_bench_gui.png");
    private int texW, texH;

    public PrismFocusBenchScreen(PrismFocusBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.titleLabelX = this.leftPos+8;
        this.titleLabelY = (this.height - this.texH)/2-55;
        this.inventoryLabelX = this.leftPos+8;
        this.inventoryLabelY = this.topPos + 103;
    }

    @Override
    protected void init() {
        super.init();
        int buttonX = getGuiLeft() + 71;
        int buttonY = getGuiTop() + 76;

        this.addRenderableWidget(Button.builder(
            Component.literal("Converge"),
            button -> menu.onConvergePress())
            .pos(buttonX, buttonY)
            .size(34, 14)
            .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI_TEX);

        texW = 256;
        texH = 256;

        int x = (width - texW) / 2;
        int y = (height - texH) / 2;

        guiGraphics.blit(GUI_TEX, x, y, 0, 0, texW, texH, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
