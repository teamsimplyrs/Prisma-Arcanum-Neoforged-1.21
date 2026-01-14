package com.teamsimplyrs.prismaarcanum.entity.client.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.ManaPelletProjectile;
import com.teamsimplyrs.prismaarcanum.spells.mentis.ManaPellet;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ManaPelletRenderer extends EntityRenderer<ManaPelletProjectile> {
    private ManaPelletModel model;

    public ManaPelletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ManaPelletModel(context.bakeLayer(ManaPelletModel.LAYER_LOCATION));
    }

    @Override
    public void render(ManaPelletProjectile p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(p_entity.getRotationVector().y * 5f));
        poseStack.mulPose(Axis.XP.rotationDegrees(p_entity.getRotationVector().x * 5f));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(p_entity)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();

        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ManaPelletProjectile manaPelletProjectile) {
        Element element = ManaPellet.element;
        String id = ManaPellet.spellID;
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, String.format("%s/%s/%s.png", "textures/entity/spells", element.toString().toLowerCase(), id));
    }
}
