package com.teamsimplyrs.prismaarcanum.api.spell.states;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntitySpellControlStateComponent {
    private final List<EntitySpellControlStateInstance> states = new ArrayList<>();

    public void addState(EntitySpellControlStateInstance state) {
        states.add(state);
    }

    public boolean hasState(EntitySpellControlStateInstance state) {
        return states.contains(state);
    }

    public boolean isEmpty() {
        return states.isEmpty();
    }

    public void tick(Entity entity) {
        if (states.isEmpty()) {
            return;
        }

        List<EntitySpellControlStateInstance> nextStates = new ArrayList<>();
        List<EntitySpellControlStateInstance> expiredStates = new ArrayList<>();

        for (EntitySpellControlStateInstance state: states) {
            state.tick(entity);
            if (state.isFinished()) {
                expiredStates.add(state);
                var next = state.getNextState();
                if (next != null) {
                    nextStates.add(state.getNextState());
                }
            }
        }

        states.removeAll(expiredStates);
        states.addAll(nextStates);
    }
}
