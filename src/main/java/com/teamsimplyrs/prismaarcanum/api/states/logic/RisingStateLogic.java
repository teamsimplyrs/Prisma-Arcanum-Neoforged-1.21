package com.teamsimplyrs.prismaarcanum.api.states.logic;

import com.teamsimplyrs.prismaarcanum.api.states.EntitySpellControlStateInstance;
import com.teamsimplyrs.prismaarcanum.api.states.data.RisingStateData;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RisingStateLogic implements IEntitySpellControlStateLogic {
    @Override
    public void onStart(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
        entity.hurtMarked = true;
    }

    @Override
    public void onTick(Entity entity, EntitySpellControlStateInstance state) {
        if (state.getData() instanceof RisingStateData(Vec3 riseVelocity)) {
            Vec3 delta = entity.getDeltaMovement();
            entity.setDeltaMovement(delta.add(riseVelocity));
        }
    }

    @Override
    public void onEnd(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }
}
