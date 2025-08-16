package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ChainAction implements ISpellAction {
    public int hops;
    public float radius_x;
    public float radius_y;
    public float radius_z;
    public float effect_dropoff;

    public ChainAction(int hops, float radius_x, float radius_y, float radius_z, float effect_dropoff) {
        this.hops = hops;
        this.radius_x = radius_x;
        this.radius_y = radius_y;
        this.radius_z = radius_z;
        this.effect_dropoff = effect_dropoff;
    }

    @Override
    public void play(ServerLevel level, ServerPlayer player) {

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

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
