package com.teamsimplyrs.prismaarcanum.system.spellsystem.actions;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BeamAction extends GenericAction {
    public float width;
    public float height;
    public float range;
    public ISpellAction hit_components;

    public BeamAction(float width, float height, float range, ISpellAction hit_component, boolean isBlocking) {
        super(isBlocking);
        this.width = width;
        this.height = height;
        this.range = range;
        this.hit_components = hit_component;
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
