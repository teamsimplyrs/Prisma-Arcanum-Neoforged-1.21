package com.teamsimplyrs.prismaarcanum.entity.client.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.IgniumLegionnaireEntity;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class IgniumLegionnaireRenderer extends MobRenderer<IgniumLegionnaireEntity, IgniumLegionnaireModel<IgniumLegionnaireEntity>> {
    public IgniumLegionnaireRenderer(EntityRendererProvider.Context context) {
        super(context, new IgniumLegionnaireModel<>(context.bakeLayer(IgniumLegionnaireModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(IgniumLegionnaireEntity igniumLegionnaireEntity) {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/entity/monsters/ignium_legionnaire.png");
    }

    @Override
    public void render(IgniumLegionnaireEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
