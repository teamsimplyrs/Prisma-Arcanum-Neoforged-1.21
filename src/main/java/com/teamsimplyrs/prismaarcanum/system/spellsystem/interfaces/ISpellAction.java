package com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ISpellAction {
    public void play(ServerLevel level, ServerPlayer player);
    public void pause();
    public void resume();
    public void abort();
    public boolean isComplete();
    public boolean isBlocking();
}
