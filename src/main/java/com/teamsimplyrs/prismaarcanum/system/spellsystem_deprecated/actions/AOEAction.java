package com.teamsimplyrs.prismaarcanum.system.spellsystem_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AOEAction extends GenericAction {
    public float radius_x;
    public float radius_y;
    public float radius_z;
    public GenericAction hit_component;

    public AOEAction(float radius_x, float radius_y, float radius_z, GenericAction hit_component, boolean isBlocking) {
        super(isBlocking);
        this.radius_x = radius_x;
        this.radius_y = radius_y;
        this.radius_z = radius_z;
        this.hit_component = hit_component;
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
