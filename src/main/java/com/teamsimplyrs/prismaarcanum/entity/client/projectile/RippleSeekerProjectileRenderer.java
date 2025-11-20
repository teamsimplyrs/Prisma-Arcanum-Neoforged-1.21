package com.teamsimplyrs.prismaarcanum.entity.client.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.RippleSeekerProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RippleSeekerProjectileRenderer extends EntityRenderer<RippleSeekerProjectile> {
    private RippleSeekerProjectileModel model;

    public RippleSeekerProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RippleSeekerProjectileModel(context.bakeLayer(RippleSeekerProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(RippleSeekerProjectile p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(p_entity)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();

        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RippleSeekerProjectile rippleSeekerProjectile) {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/entity/monsters/seekers/ripple_seeker_projectile.png");
    }
}
