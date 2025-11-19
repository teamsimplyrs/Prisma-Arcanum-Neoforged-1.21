package com.teamsimplyrs.prismaarcanum.entity.client.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RippleSeekerRenderer extends MobRenderer<RippleSeekerEntity, RippleSeekerModel<RippleSeekerEntity>> {
    public RippleSeekerRenderer(EntityRendererProvider.Context context) {
        super(context, new RippleSeekerModel<>(context.bakeLayer(RippleSeekerModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(RippleSeekerEntity rippleSeekerEntity) {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "textures/entity/monsters/seekers/ripple_seeker.png");
    }

    @Override
    public void render(RippleSeekerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
