package com.teamsimplyrs.prismaarcanum.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpellEffectAreaEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SpellEffectAreaRenderer extends EntityRenderer<SpellEffectAreaEntity> {
    private EntityModel<SpellEffectAreaEntity> model;

    public SpellEffectAreaRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SpellEffectAreaEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SpellEffectAreaEntity entity) {
        return null;
    }
}
