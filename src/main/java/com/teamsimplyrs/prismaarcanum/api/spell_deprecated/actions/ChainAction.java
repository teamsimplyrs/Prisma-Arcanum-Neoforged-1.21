package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ChainAction extends GenericAction {
    public int hops;
    public float radius_x;
    public float radius_y;
    public float radius_z;
    public float effect_dropoff;

    public ChainAction(int hops, float radius_x, float radius_y, float radius_z, float effect_dropoff, boolean isBlocking) {
        super(isBlocking);
        this.hops = hops;
        this.radius_x = radius_x;
        this.radius_y = radius_y;
        this.radius_z = radius_z;
        this.effect_dropoff = effect_dropoff;
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
