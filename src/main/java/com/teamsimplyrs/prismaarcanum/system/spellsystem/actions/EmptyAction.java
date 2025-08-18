package com.teamsimplyrs.prismaarcanum.system.spellsystem.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EmptyAction extends GenericAction {

    public EmptyAction() {
        super(false);
    }

    @Override
    public void play(Level level, Player player) {

    }

    @Override
    public void tick(Level level, Player player) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void abort() {

    }
}
