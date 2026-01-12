package com.teamsimplyrs.prismaarcanum.api.spell.states.logic;

import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import com.teamsimplyrs.prismaarcanum.api.spell.states.data.SlammingStateData;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SlammingStateLogic implements IEntitySpellControlStateLogic {
    private Vec3 initialPosition;

    @Override
    public void onStart(Entity entity, EntitySpellControlStateInstance state) {
        entity.setNoGravity(false);
        initialPosition = entity.position();

        if (state.getData() instanceof SlammingStateData(Vec3 slamVelocity)) {
            entity.setDeltaMovement(slamVelocity);
            entity.hurtMarked = true;
        }
    }

    @Override
    public void onTick(Entity entity, EntitySpellControlStateInstance state) {
        if (initialPosition == null) {
            initialPosition = entity.position();
        }

        if (state.getData() instanceof SlammingStateData(Vec3 slamVelocity)) {
            Vec3 delta = entity.getDeltaMovement();
            Vec3 newDelta = new Vec3(delta.x, Mth.clamp(delta.y, slamVelocity.y, 0), delta.z); // assuming that the Y component is always negative for slamming
            entity.setDeltaMovement(newDelta);
        }
    }

    @Override
    public void onEnd(Entity entity, EntitySpellControlStateInstance state) {
        entity.hurtMarked = true;
        entity.setNoGravity(false);

        Runnable onEnd = state.getStateEndRunner();
        if (onEnd != null) {
            onEnd.run();
        }
    }

    @Override
    public boolean shouldForceEnd(Entity entity, EntitySpellControlStateInstance state) {
        if (entity.onGround() && state.getData() instanceof SlammingStateData(Vec3 slamVelocity) && initialPosition != null) {
            return entity.position().y < initialPosition.y - 0.3f || entity.fallDistance >= 0.25f;
        }

        return false;
    }
}
