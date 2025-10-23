package com.teamsimplyrs.prismaarcanum.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.ignis.FireballSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.entity.custom.FireballSpellProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FireballSpellProjectileRenderer extends EntityRenderer<FireballSpellProjectile> {
    private FireballSpellProjectileModel model;

    public FireballSpellProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FireballSpellProjectileModel(context.bakeLayer(FireballSpellProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(FireballSpellProjectile p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(p_entity)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();

        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FireballSpellProjectile fireballSpellProjectile) {
        Element element = FireballSpell.element;
        String id = FireballSpell.spellID;
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, String.format("%s/%s/%s.png", "textures/entity/spells", element.toString().toLowerCase(), id));
    }
}
