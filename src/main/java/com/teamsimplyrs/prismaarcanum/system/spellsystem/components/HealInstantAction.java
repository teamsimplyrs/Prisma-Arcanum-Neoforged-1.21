package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HealInstantAction implements ISpellAction {
    public float amount;

    public HealInstantAction(float amount) {
        this.amount = amount;
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
