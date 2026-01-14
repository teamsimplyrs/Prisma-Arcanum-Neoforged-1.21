package com.teamsimplyrs.prismaarcanum.api.states.data;

import net.minecraft.world.phys.Vec3;

public record SlammingStateData(Vec3 slamVelocity) implements IEntitySpellControlStateData {
}
