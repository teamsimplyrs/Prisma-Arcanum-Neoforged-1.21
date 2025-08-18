package com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ISpellAction {
    void play(Level level, Player player);
    void tick(Level level, Player player);
    void pause();
    void resume();
    void abort();
}
