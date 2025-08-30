package com.teamsimplyrs.prismaarcanum.system.spellsystem_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HealInstantAction extends GenericAction {
    public float amount;

    public HealInstantAction(float amount, boolean isBlocking) {
        super(isBlocking);
        this.amount = amount;
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
