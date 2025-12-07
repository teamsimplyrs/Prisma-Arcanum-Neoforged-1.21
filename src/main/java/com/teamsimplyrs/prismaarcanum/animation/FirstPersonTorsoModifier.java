package com.teamsimplyrs.prismaarcanum.animation;

import com.mojang.logging.LogUtils;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.slf4j.Logger;

public class FirstPersonTorsoModifier extends AbstractModifier {
    protected static final Logger LOGGER = LogUtils.getLogger();
    @Override
    public PlayerAnimBone get3DTransform(PlayerAnimBone bone) {
        // only affect torso
        if (!"body".equals(bone.getName())) {
            return super.get3DTransform(bone);
        }

        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) {
            LOGGER.info("player null");
            return super.get3DTransform(bone);
        }

        // Only apply in first-person camera
//        if (!mc.options.getCameraType().isFirstPerson()) {
//            LOGGER.info("is not first person");
//            return super.get3DTransform(bone);
//        }
        LOGGER.info("starting vector "+bone.getRotationVector());

        float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(true); // Provided by AbstractModifier superclass

        // View direction (camera look yaw)
        float viewYaw = player.getViewYRot(partialTick);

        // Body yaw (torso base orientation)
        float bodyYaw = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot);

        // Yaw difference — how much we need torso to rotate to match view
        float deltaYawDeg = Mth.wrapDegrees(viewYaw - bodyYaw);
        float deltaYawRad = deltaYawDeg * Mth.DEG_TO_RAD;

        // Apply properly to a cloned bone like PAL expects
        PlayerAnimBone clone = new PlayerAnimBone(bone.getName());
        clone.copyOtherBone(bone);

        // Let other animation chain run first
        clone = super.get3DTransform(clone);

        // Now apply our override
        clone.rotY += deltaYawRad;

        // Write back changes to original
        bone.copyOtherBone(clone);
        LOGGER.info("reaches the end "+bone.getRotationVector());
      //  bone.scale(2);
        return bone;
    }
}