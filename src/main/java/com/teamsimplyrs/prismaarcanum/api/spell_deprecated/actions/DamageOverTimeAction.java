package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DamageOverTimeAction extends GenericAction {
    public float amount;
    public float duration;
    public float interval;

    public DamageOverTimeAction(float amount, float duration, float interval, boolean isBlocking) {
        super(isBlocking);
        this.amount = amount;
        this.duration = duration;
        this.interval = interval;
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
