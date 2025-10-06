package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KnockbackAction extends GenericAction {
    public float strength;

    public KnockbackAction(float strength, boolean isBlocking) {
        super(isBlocking);
        this.strength = strength;
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
