package com.teamsimplyrs.prismaarcanum.entity.client.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.IgniumLegionnaireEntity;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;

public class IgniumLegionnaireModel<T extends IgniumLegionnaireEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "ignium_legionnaire"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart cranium_parts;
    private final ModelPart halo_small;
    private final ModelPart halo_large;
    private final ModelPart cloth_head;
    private final ModelPart torso_top;
    private final ModelPart cloth_front;
    private final ModelPart cloth_back;
    private final ModelPart cloth_left;
    private final ModelPart cloth_right;
    private final ModelPart legs;
    private final ModelPart legR;
    private final ModelPart topL;
    private final ModelPart bottomL;
    private final ModelPart legL;
    private final ModelPart topR;
    private final ModelPart bottomR;
    private final ModelPart arms;
    private final ModelPart armL;
    private final ModelPart armR;

    public IgniumLegionnaireModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.cranium_parts = this.head.getChild("cranium_parts");
        this.halo_small = this.head.getChild("halo_small");
        this.halo_large = this.head.getChild("halo_large");
        this.cloth_head = this.head.getChild("cloth_head");
        this.torso_top = this.root.getChild("torso_top");
        this.cloth_front = this.torso_top.getChild("cloth_front");
        this.cloth_back = this.torso_top.getChild("cloth_back");
        this.cloth_left = this.torso_top.getChild("cloth_left");
        this.cloth_right = this.torso_top.getChild("cloth_right");
        this.legs = this.root.getChild("legs");
        this.legR = this.legs.getChild("legR");
        this.topL = this.legR.getChild("topL");
        this.bottomL = this.legR.getChild("bottomL");
        this.legL = this.legs.getChild("legL");
        this.topR = this.legL.getChild("topR");
        this.bottomR = this.legL.getChild("bottomR");
        this.arms = this.root.getChild("arms");
        this.armL = this.arms.getChild("armL");
        this.armR = this.arms.getChild("armR");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -32.0F, 0.0F));

        PartDefinition gem_r1 = head.addOrReplaceChild("gem_r1", CubeListBuilder.create().texOffs(58, 86).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, -0.6376F, 0.0F, 0.0F, 0.7854F));

        PartDefinition helm_r1 = head.addOrReplaceChild("helm_r1", CubeListBuilder.create().texOffs(32, 41).addBox(-6.0F, -6.0F, -1.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.25F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cranium_parts = head.addOrReplaceChild("cranium_parts", CubeListBuilder.create().texOffs(0, 30).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(1.25F, 0.0F, -4.25F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.25F, 0.0F, -4.25F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition halo_small = head.addOrReplaceChild("halo_small", CubeListBuilder.create().texOffs(52, 13).addBox(-4.5F, -0.75F, -6.5F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 66).addBox(-6.5F, -0.75F, 4.5F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 69).addBox(-1.0F, -1.0F, -8.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(86, 23).addBox(-1.0F, -1.0F, 5.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(84, 50).addBox(-8.5F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 86).addBox(5.5F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.75F, -0.5F));

        PartDefinition ring_part4_r1 = halo_small.addOrReplaceChild("ring_part4_r1", CubeListBuilder.create().texOffs(50, 63).addBox(-5.0F, -1.0F, -1.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 0.25F, -0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition ring_part3_r1 = halo_small.addOrReplaceChild("ring_part3_r1", CubeListBuilder.create().texOffs(24, 63).addBox(-5.0F, -1.0F, -1.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.25F, 1.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition halo_large = head.addOrReplaceChild("halo_large", CubeListBuilder.create().texOffs(32, 35).addBox(-10.0F, -0.3125F, 8.0F, 18.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 38).addBox(-8.0F, -0.3125F, -10.0F, 18.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(26, 66).addBox(-1.0F, -0.5625F, -14.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(46, 66).addBox(-1.0F, -0.5625F, 6.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(68, 59).addBox(-14.0F, -0.5625F, -1.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 0).addBox(6.0F, -0.5625F, -1.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(82, 71).addBox(4.0F, -0.5625F, 7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 83).addBox(-6.0F, -0.5625F, 7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 83).addBox(-6.0F, -0.5625F, -11.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 83).addBox(4.0F, -0.5625F, -11.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.9375F, 0.0F));

        PartDefinition tooth12_r1 = halo_large.addOrReplaceChild("tooth12_r1", CubeListBuilder.create().texOffs(84, 40).addBox(-6.0F, -0.5F, 7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(84, 45).addBox(4.0F, -0.5F, 7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(86, 13).addBox(4.0F, -0.5F, -11.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(86, 18).addBox(-6.0F, -0.5F, -11.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0625F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition ring_part4_r2 = halo_large.addOrReplaceChild("ring_part4_r2", CubeListBuilder.create().texOffs(32, 32).addBox(-5.0F, -1.0F, -1.0F, 18.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 0.6875F, 3.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition ring_part1_r1 = halo_large.addOrReplaceChild("ring_part1_r1", CubeListBuilder.create().texOffs(32, 29).addBox(-10.0F, -1.0F, -1.0F, 18.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 0.6875F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cloth_head = head.addOrReplaceChild("cloth_head", CubeListBuilder.create().texOffs(0, 45).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.75F, 0.0F));

        PartDefinition clothR_r1 = cloth_head.addOrReplaceChild("clothR_r1", CubeListBuilder.create().texOffs(76, 62).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.75F, -3.5F, 0.0F, 0.0F, 1.5708F, -0.5236F));

        PartDefinition clothL_r1 = cloth_head.addOrReplaceChild("clothL_r1", CubeListBuilder.create().texOffs(76, 11).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.75F, -3.5F, 0.0F, 0.0F, 1.5708F, 0.5236F));

        PartDefinition clothB_r1 = cloth_head.addOrReplaceChild("clothB_r1", CubeListBuilder.create().texOffs(76, 9).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, 4.75F, -0.5236F, 0.0F, 0.0F));

        PartDefinition clothF_r1 = cloth_head.addOrReplaceChild("clothF_r1", CubeListBuilder.create().texOffs(52, 16).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, -4.75F, 0.5236F, 0.0F, 0.0F));

        PartDefinition torso_top = root.addOrReplaceChild("torso_top", CubeListBuilder.create().texOffs(73, 93).addBox(-2.0F, -25.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -28.0F, -6.0F, 14.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(40, 18).addBox(-4.0F, -22.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(68, 53).addBox(-3.0F, -19.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-6.0F, -17.0F, -4.0F, 12.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cloth_front = torso_top.addOrReplaceChild("cloth_front", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0405F, -3.9392F));

        PartDefinition clothF_extension_r1 = cloth_front.addOrReplaceChild("clothF_extension_r1", CubeListBuilder.create().texOffs(52, 0).addBox(-6.0F, -5.5F, 0.0F, 12.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.4044F, -0.9941F, -0.0873F, 0.0F, 0.0F));

        PartDefinition clothF_r2 = cloth_front.addOrReplaceChild("clothF_r2", CubeListBuilder.create().texOffs(60, 41).addBox(-6.0F, -1.5F, 0.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.4589F, -0.4634F, -0.1745F, 0.0F, 0.0F));

        PartDefinition cloth_back = torso_top.addOrReplaceChild("cloth_back", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, 4.25F));

        PartDefinition clothB_r2 = cloth_back.addOrReplaceChild("clothB_r2", CubeListBuilder.create().texOffs(60, 47).addBox(-6.0F, -1.5F, 0.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.4935F, 0.1609F, 2.9671F, 0.0F, 3.1416F));

        PartDefinition clothB_extension_r1 = cloth_back.addOrReplaceChild("clothB_extension_r1", CubeListBuilder.create().texOffs(0, 53).addBox(-6.0F, -5.5F, 0.0F, 12.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.189F, 0.6301F, 3.0543F, 0.0F, 3.1416F));

        PartDefinition cloth_left = torso_top.addOrReplaceChild("cloth_left", CubeListBuilder.create(), PartPose.offset(5.9187F, -17.0096F, 0.0183F));

        PartDefinition clothL_r2 = cloth_left.addOrReplaceChild("clothL_r2", CubeListBuilder.create().texOffs(76, 3).addBox(-4.0F, -1.5F, 0.0F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4358F, 1.5642F, 0.0111F, 0.0F, -1.5708F, -0.1745F));

        PartDefinition cloth_right = torso_top.addOrReplaceChild("cloth_right", CubeListBuilder.create(), PartPose.offset(-6.0F, -17.0F, 0.0F));

        PartDefinition clothR_r2 = cloth_right.addOrReplaceChild("clothR_r2", CubeListBuilder.create().texOffs(48, 75).addBox(-4.0F, -1.5F, 0.0F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 1.5F, 0.0F, 0.0F, -1.5708F, 0.1745F));

        PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legR = legs.addOrReplaceChild("legR", CubeListBuilder.create(), PartPose.offset(-3.25F, -12.0F, 0.0F));

        PartDefinition topL = legR.addOrReplaceChild("topL", CubeListBuilder.create().texOffs(24, 55).addBox(-2.0F, -2.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(16, 75).addBox(-1.5F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bottomL = legR.addOrReplaceChild("bottomL", CubeListBuilder.create().texOffs(82, 64).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(86, 33).addBox(-1.0F, 2.0F, 0.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.0F, -1.5F));

        PartDefinition legL = legs.addOrReplaceChild("legL", CubeListBuilder.create(), PartPose.offset(3.25F, -12.0F, 0.0F));

        PartDefinition topR = legL.addOrReplaceChild("topR", CubeListBuilder.create().texOffs(32, 75).addBox(-2.5F, -2.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 55).addBox(-3.0F, -4.0F, -2.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -1.0F));

        PartDefinition bottomR = legL.addOrReplaceChild("bottomR", CubeListBuilder.create().texOffs(76, 82).addBox(-2.5F, -2.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(86, 27).addBox(-2.0F, 2.0F, 0.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.0F, -1.5F));

        PartDefinition arms = root.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armL = arms.addOrReplaceChild("armL", CubeListBuilder.create().texOffs(66, 66).addBox(-2.0F, -1.95F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 86).addBox(-1.0F, 5.05F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 77).addBox(-1.5F, 7.05F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(76, 77).addBox(-2.0F, 13.05F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -25.05F, 0.0F));

        PartDefinition shoulder_r1 = armL.addOrReplaceChild("shoulder_r1", CubeListBuilder.create().texOffs(72, 16).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.05F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition armR = arms.addOrReplaceChild("armR", CubeListBuilder.create().texOffs(0, 69).addBox(9.0F, -27.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 88).addBox(10.0F, -20.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 80).addBox(9.5F, -18.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-21.0F, 0.0F, 0.0F));

        PartDefinition shoulder_r2 = armR.addOrReplaceChild("shoulder_r2", CubeListBuilder.create().texOffs(72, 28).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.5F, -25.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30, 30);
        headPitch = Mth.clamp(headPitch, -30, 30);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch * ((float)Math.PI / 180f);
    }

    @Override
    public void setupAnim(IgniumLegionnaireEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

//        this.animateWalk(RippleSeekerAnimations.ANIM_RIPPLE_SEEKER_IDLE, limbSwing, limbSwingAmount, 1f, 1f);
        this.animate(entity.idleAnimState, IgniumLegionnaireAnimations.IGNIUM_LEGIONNAIRE_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
