package com.teamsimplyrs.prismaarcanum.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * A generic spell renderer for any simple entity that uses the same ManaPellet model and rendering style.
 * @param <T> The entity type
 * (ManaPellet model has to be removed later and switched to an invisible entity renderer)
 */
public class GenericEmptyRenderer<T extends Entity> extends EntityRenderer<T> {

    public GenericEmptyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }
}
