package com.teamsimplyrs.prismaarcanum.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.utils.GuiUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class WandRenderer extends BlockEntityWithoutLevelRenderer {

    private static final Minecraft MC = Minecraft.getInstance();

    private static final ModelResourceLocation WAND_MODEL =
            ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID, "item/wand_staff"));

    private static final ModelResourceLocation GEM_MODEL =
            ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID, "item/gem_staff"));

    public WandRenderer() {
        super(MC.getBlockEntityRenderDispatcher(), MC.getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack,
                             ItemDisplayContext context,
                             PoseStack poseStack,
                             MultiBufferSource buffer,
                             int packedLight,
                             int packedOverlay) {

        BakedModel wandModel = MC.getModelManager().getModel(WAND_MODEL);
        BakedModel gemModel = MC.getModelManager().getModel(GEM_MODEL);

        boolean rotate = (context != ItemDisplayContext.GUI &&
                context != ItemDisplayContext.GROUND &&
                context != ItemDisplayContext.FIXED);

        // ===== Render Rotating Crystal =====
        poseStack.pushPose();
        if (rotate) {
            float time = MC.level.getGameTime() + MC.getTimer().getGameTimeDeltaPartialTick(true);
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 20f));
        }

        int spellColor = 0xDE6B23;
        var spell = WandUtils.getCurrentSpell(stack);
        if (spell != null) {
            spellColor = GuiUtils.getTextColorForElement(spell.getElement());
        }

        // ===== Render Gem Handle =====
        renderBakedModelColored(
                poseStack, buffer, gemModel, stack,
                packedLight, packedOverlay,
                spellColor, RenderType.translucent()
        );
        poseStack.popPose();

        // ===== Render Wand Handle =====
        poseStack.pushPose();
        renderBakedModelColored(
                poseStack, buffer, wandModel, stack,
                packedLight, packedOverlay,
                0xFFFFFF, RenderType.solid()
        );
        poseStack.popPose();
    }

    private static void renderBakedModelColored(
            PoseStack poseStack,
            MultiBufferSource buffer,
            BakedModel model,
            ItemStack stack,
            int light, int overlay,
            int color,
            RenderType type
    ) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        var consumer = buffer.getBuffer(type);
        var pose = poseStack.last();

        for (var quad : model.getQuads(null, null, null)) {
            consumer.putBulkData(pose, quad, r, g, b, 1f, light, overlay);
        }
    }
}