package com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public interface ICastable {
    public void cast(Level world, Player player, SpellDataModel spellData);

    public HitResult raycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance) ;

    HitResult blockRaycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance);

    HitResult entityRaycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance);

    public void upgrade();
}
