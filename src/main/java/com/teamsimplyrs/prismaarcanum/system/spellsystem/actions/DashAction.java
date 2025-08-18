package com.teamsimplyrs.prismaarcanum.system.spellsystem.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DashAction extends GenericAction {
    public float distance;
    public float duration;

    public DashAction(float distance, float duration, boolean isBlocking) {
        super(isBlocking);
        this.distance = distance;
        this.duration = duration;
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
