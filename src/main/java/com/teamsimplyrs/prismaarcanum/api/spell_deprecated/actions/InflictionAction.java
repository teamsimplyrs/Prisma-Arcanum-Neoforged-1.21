package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class InflictionAction extends GenericAction {
    public float duration;
    public GenericAction component;

    public InflictionAction(float duration, GenericAction hit_component, boolean isBlocking) {
        super(isBlocking);
        this.duration = duration;
        this.component = hit_component;
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
