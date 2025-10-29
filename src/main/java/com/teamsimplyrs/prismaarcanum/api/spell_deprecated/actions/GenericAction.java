package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions;

import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.interfaces.ISpellAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class GenericAction implements ISpellAction {
    public final boolean isBlocking;
    public boolean isCompleted;
    public boolean isStarted;
    public boolean isPaused;
    public boolean isCancelled;

    protected GenericAction(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }

    @Override
    public void play(Level level, Player player) {
        isStarted = true;
    }


}
