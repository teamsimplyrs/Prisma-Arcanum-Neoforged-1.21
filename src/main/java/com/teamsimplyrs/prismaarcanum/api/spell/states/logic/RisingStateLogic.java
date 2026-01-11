package com.teamsimplyrs.prismaarcanum.api.spell.states.logic;

import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import com.teamsimplyrs.prismaarcanum.api.spell.states.data.RisingStateData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RisingStateLogic implements IEntitySpellControlStateLogic {
    @Override
    public void onStart(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void onTick(Entity entity, EntitySpellControlStateInstance state) {
        if (state.getData() instanceof RisingStateData(Vec3 riseVelocity)) {
            entity.setDeltaMovement(riseVelocity);
        }
    }

    @Override
    public void onEnd(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
    }
}
