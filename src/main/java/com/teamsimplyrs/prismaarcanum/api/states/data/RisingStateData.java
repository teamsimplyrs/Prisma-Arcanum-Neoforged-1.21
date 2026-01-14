package com.teamsimplyrs.prismaarcanum.api.states.data;

import net.minecraft.world.phys.Vec3;

public record RisingStateData(Vec3 riseVelocity) implements IEntitySpellControlStateData {

}
