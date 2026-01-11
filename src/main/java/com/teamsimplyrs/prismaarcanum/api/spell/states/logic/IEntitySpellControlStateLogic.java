package com.teamsimplyrs.prismaarcanum.api.spell.states.logic;

import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import net.minecraft.world.entity.Entity;

public interface IEntitySpellControlStateLogic {

    void onStart(Entity entity, EntitySpellControlStateInstance state);
    void onTick(Entity entity, EntitySpellControlStateInstance state);
    void onEnd(Entity entity, EntitySpellControlStateInstance state);
}
