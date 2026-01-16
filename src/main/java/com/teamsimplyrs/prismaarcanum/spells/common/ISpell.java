package com.teamsimplyrs.prismaarcanum.spells.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface ISpell {
    void cast(ServerPlayer player, Level world);

    ResourceLocation getResourceLocation();
}
