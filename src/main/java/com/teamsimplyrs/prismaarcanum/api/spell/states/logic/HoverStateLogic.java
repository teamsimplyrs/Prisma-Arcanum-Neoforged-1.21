package com.teamsimplyrs.prismaarcanum.api.spell.states.logic;

import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class HoverStateLogic implements IEntitySpellControlStateLogic {

    @Override
    public void onStart(Entity entity, EntitySpellControlStateInstance state) {
        entity.setNoGravity(true);
    }

    @Override
    public void onTick(Entity entity, EntitySpellControlStateInstance state) {
        entity.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void onEnd(Entity entity, EntitySpellControlStateInstance state) {
        entity.setNoGravity(false);
    }
}
