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
        if (!"body".equals(bone.getName())) {
            return super.get3DTransform(bone);
        }

        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) {
            return super.get3DTransform(bone);
        }

        // Only apply in first-person camera
//        if (!mc.options.getCameraType().isFirstPerson()) {
//            LOGGER.info("is not first person");
//            return super.get3DTransform(bone);
//        }

        float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(true); // Provided by AbstractModifier superclass

        float viewYaw = player.getViewYRot(partialTick);

        float bodyYaw = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot);

        float deltaYawDeg = Mth.wrapDegrees(viewYaw - bodyYaw);
        float deltaYawRad = deltaYawDeg * Mth.DEG_TO_RAD;

        PlayerAnimBone clone = new PlayerAnimBone(bone.getName());
        clone.copyOtherBone(bone);

        clone = super.get3DTransform(clone);

        clone.rotY += deltaYawRad;

        bone.copyOtherBone(clone);
        return bone;
    }
}