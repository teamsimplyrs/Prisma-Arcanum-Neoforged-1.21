package com.teamsimplyrs.prismaarcanum.entity.client.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class RippleSeekerModel<T extends RippleSeekerEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "ripple_seeker"), "main");
    private final ModelPart whole;
    private final ModelPart main;
    private final ModelPart appendages;
    private final ModelPart dorsal_fin;
    private final ModelPart ventral_fin;
    private final ModelPart left_fin;
    private final ModelPart right_fin;
    private final ModelPart tail;

    public RippleSeekerModel(ModelPart root) {
        this.whole = root.getChild("whole");
        this.main = this.whole.getChild("main");
        this.appendages = this.whole.getChild("appendages");
        this.dorsal_fin = this.appendages.getChild("dorsal_fin");
        this.ventral_fin = this.appendages.getChild("ventral_fin");
        this.left_fin = this.appendages.getChild("left_fin");
        this.right_fin = this.appendages.getChild("right_fin");
        this.tail = this.appendages.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition whole = partdefinition.addOrReplaceChild("whole", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition main = whole.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -2.5F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-4.0F, -4.0F, -3.5F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 10).addBox(-4.0F, -4.0F, 7.5F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(22, 50).addBox(-2.0F, -2.0F, 9.5F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0578F, -2.0F, -3.4978F, 0.0F, 0.0F, -0.7854F));

        PartDefinition appendages = whole.addOrReplaceChild("appendages", CubeListBuilder.create().texOffs(48, 42).addBox(-0.5F, -11.25F, -10.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(50, 20).addBox(-0.5F, 2.25F, -10.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(54, 37).addBox(5.25F, -4.5F, -6.75F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 56).addBox(-7.25F, -4.5F, -6.75F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 40).addBox(-1.0F, -4.5F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0578F, 2.0F, 5.0022F));

        PartDefinition dorsal_fin = appendages.addOrReplaceChild("dorsal_fin", CubeListBuilder.create().texOffs(0, 20).addBox(0.5F, -10.0F, 1.0F, 0.0F, 11.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(46, 56).addBox(0.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -12.25F, -10.0F));

        PartDefinition dorsal_4_r1 = dorsal_fin.addOrReplaceChild("dorsal_4_r1", CubeListBuilder.create().texOffs(28, 56).addBox(-0.5F, 1.0F, 0.25F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -10.5F, 15.0F, -1.3963F, 0.0F, 0.0F));

        PartDefinition dorsal_3_r1 = dorsal_fin.addOrReplaceChild("dorsal_3_r1", CubeListBuilder.create().texOffs(34, 55).addBox(-0.5F, -3.4678F, -0.4822F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -7.5F, 8.5F, -1.0472F, 0.0F, 0.0F));

        PartDefinition dorsal_2_r1 = dorsal_fin.addOrReplaceChild("dorsal_2_r1", CubeListBuilder.create().texOffs(54, 51).addBox(-0.5F, -6.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.45F, 1.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition ventral_fin = appendages.addOrReplaceChild("ventral_fin", CubeListBuilder.create().texOffs(26, 20).addBox(0.5F, -1.0F, 1.0F, 0.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(50, 56).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 4.25F, -10.0F));

        PartDefinition ventral_4_r1 = ventral_fin.addOrReplaceChild("ventral_4_r1", CubeListBuilder.create().texOffs(38, 55).addBox(-0.5F, -2.0F, 0.25F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.35F, 11.15F, -1.5708F, 0.0F, 0.0F));

        PartDefinition ventral_3_r1 = ventral_fin.addOrReplaceChild("ventral_3_r1", CubeListBuilder.create().texOffs(22, 44).addBox(-0.5F, -0.75F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.9F, 6.5F, -1.8762F, 0.0F, 0.0F));

        PartDefinition ventral_2_r1 = ventral_fin.addOrReplaceChild("ventral_2_r1", CubeListBuilder.create().texOffs(42, 56).addBox(-0.5F, 0.75F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 3.0F, 2.5F, -2.3126F, 0.0F, 0.0F));

        PartDefinition left_fin = appendages.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(26, 42).addBox(0.52F, -3.5048F, 0.0F, 11.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(1.02F, -0.5048F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.23F, -3.9952F, -5.75F));

        PartDefinition bone_3_r1 = left_fin.addOrReplaceChild("bone_3_r1", CubeListBuilder.create().texOffs(50, 29).addBox(0.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.77F, -2.2548F, 0.5F, 0.0F, 0.0F, -0.1309F));

        PartDefinition bone_2_r1 = left_fin.addOrReplaceChild("bone_2_r1", CubeListBuilder.create().texOffs(50, 33).addBox(0.0F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.02F, 0.4952F, 0.5F, 0.0F, 0.0F, -0.6109F));

        PartDefinition right_fin = appendages.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(0, 44).addBox(0.52F, -3.5F, -0.5F, 11.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 57).addBox(1.02F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.23F, -4.0F, -6.25F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bone_4_r1 = right_fin.addOrReplaceChild("bone_4_r1", CubeListBuilder.create().texOffs(50, 31).addBox(0.0F, -1.0F, -1.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.77F, -2.25F, 0.5F, 0.0F, 0.0F, -0.1309F));

        PartDefinition bone_3_r2 = right_fin.addOrReplaceChild("bone_3_r2", CubeListBuilder.create().texOffs(54, 35).addBox(0.0F, -1.0F, -1.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.02F, 0.5F, 0.5F, 0.0F, 0.0F, -0.6109F));

        PartDefinition tail = appendages.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 36).addBox(-4.0F, 0.0F, 3.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 1.0F));

        PartDefinition bone_right_2_r1 = tail.addOrReplaceChild("bone_right_2_r1", CubeListBuilder.create().texOffs(0, 52).addBox(-0.75F, -0.5F, 0.25F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.05F, 0.0F, 5.2F, 0.0F, -0.2618F, 0.0F));

        PartDefinition bone_right_1_r1 = tail.addOrReplaceChild("bone_right_1_r1", CubeListBuilder.create().texOffs(10, 52).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 3.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone_left_2_r1 = tail.addOrReplaceChild("bone_left_2_r1", CubeListBuilder.create().texOffs(44, 51).addBox(-0.75F, -0.5F, 0.25F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.55F, 0.0F, 5.05F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone_left_1_r1 = tail.addOrReplaceChild("bone_left_1_r1", CubeListBuilder.create().texOffs(34, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 3.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30, 30);
        headPitch = Mth.clamp(headPitch, -30, 30);

        this.whole.yRot = headYaw * ((float)Math.PI / 180f);
        this.whole.xRot = headPitch * ((float)Math.PI / 180f);
    }

    @Override
    public void setupAnim(RippleSeekerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

//        this.animateWalk(RippleSeekerAnimations.ANIM_RIPPLE_SEEKER_IDLE, limbSwing, limbSwingAmount, 1f, 1f);
        this.animate(entity.idleAnimState, RippleSeekerAnimations.ANIM_RIPPLE_SEEKER_IDLE, ageInTicks, 1f);
        this.animate(entity.attackAnimState1, RippleSeekerAnimations.ANIM_RIPPLE_SEEKER_ATTACK_1, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        whole.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return whole;
    }
}
