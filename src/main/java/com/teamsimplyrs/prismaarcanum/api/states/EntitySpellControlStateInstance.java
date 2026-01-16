package com.teamsimplyrs.prismaarcanum.api.states;

import com.teamsimplyrs.prismaarcanum.api.states.data.IEntitySpellControlStateData;
import com.teamsimplyrs.prismaarcanum.api.states.logic.IEntitySpellControlStateLogic;
import com.teamsimplyrs.prismaarcanum.api.utils.SpellUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class EntitySpellControlStateInstance {
    // region Basic Data
    private final EntitySpellControlState spellControlState;
    private final IEntitySpellControlStateLogic spellControlLogic;
    private final IEntitySpellControlStateData data;
    private int ticksLeft;
    private int delay;

    @Nullable
    private final ResourceLocation spellControlSource;

    @Nullable
    private final EntitySpellControlStateInstance nextState;

    @Nullable
    private final Runnable runOnStateEnd;
    // endregion

    // region Private data

    private boolean isStarted = false;
    private boolean isFinished = false;
    //endregion

    //region Additional Data

    @Nullable
    private Vec3 direction;

    //endregion

    public EntitySpellControlStateInstance(EntitySpellControlState state, IEntitySpellControlStateData data, int duration, int delay, @Nullable EntitySpellControlStateInstance nextState, @Nullable ResourceLocation spellSource, @Nullable Runnable runOnStateEnd) {
        this.spellControlState = state;
        this.spellControlLogic = SpellUtils.getLogicForState(state);
        this.data = data;
        this.ticksLeft = duration;
        this.delay = delay;
        this.spellControlSource = spellSource;
        this.nextState = nextState;
        this.runOnStateEnd = runOnStateEnd;
    }

    public boolean tick(Entity entity) {
        if (spellControlLogic == null) {
            return false;
        }

        if (isFinished) {
            return false;
        }

        if (spellControlLogic.shouldForceEnd(entity, this)) {
            forceEnd(entity);
        }

        if (delay > 0) {
            delay--;
            return false;
        }

        if (!isStarted) {
            spellControlLogic.onStart(entity, this);
            isStarted = true;
        }

        if (ticksLeft > 0) {
            ticksLeft--;
            spellControlLogic.onTick(entity, this);

            if (ticksLeft <= 0) {
                spellControlLogic.onEnd(entity, this);
                isFinished = true;
            }

            return true;
        }

        return false;
    }

    public void forceEnd(Entity entity) {
        if (!isFinished) {
            spellControlLogic.onEnd(entity, this);
            isFinished = true;
            ticksLeft = 0;
        }
    }

    public EntitySpellControlState getState() {
        return spellControlState;
    }

    public IEntitySpellControlStateData getData() {
        return data;
    }

    public ResourceLocation getSourceSpell() {
        return spellControlSource;
    }

    public EntitySpellControlStateInstance getNextState() {
        return nextState;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Runnable getStateEndRunner() {
        return runOnStateEnd;
    }
}
