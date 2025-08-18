package com.teamsimplyrs.prismaarcanum.system.spellsystem.actions;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class InflictionAction extends GenericAction {
    public float duration;
    public ISpellAction component;

    public InflictionAction(float duration, ISpellAction hit_component, boolean isBlocking) {
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
