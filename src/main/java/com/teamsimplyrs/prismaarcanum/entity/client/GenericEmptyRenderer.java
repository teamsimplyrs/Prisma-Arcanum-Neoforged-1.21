package com.teamsimplyrs.prismaarcanum.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.mentis.ManaPellet;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.ManaPelletModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * A generic spell renderer for any simple entity that uses the same ManaPellet model and rendering style.
 * @param <T> The entity type
 */
public class GenericEmptyRenderer<T extends Entity> extends EntityRenderer<T> {

    protected final ManaPelletModel model;

    public GenericEmptyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ManaPelletModel(context.bakeLayer(ManaPelletModel.LAYER_LOCATION));
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, String.format("%s/%s/%s.png", "textures/entity/spells", ManaPellet.element.toString().toLowerCase(), ManaPellet.spellID))));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }
}
