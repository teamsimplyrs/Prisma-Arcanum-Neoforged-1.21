package com.teamsimplyrs.prismaarcanum.api.states.logic;

import com.teamsimplyrs.prismaarcanum.api.states.EntitySpellControlStateInstance;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class HoverStateLogic implements IEntitySpellControlStateLogic {

    @Override
    public void onStart(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
        entity.setNoGravity(true);
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }

    @Override
    public void onTick(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void onEnd(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
//        entity.setNoGravity(false);
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }
}
