package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class MultiSpellEvent extends AbstractSpellTimedEvent {
    private final List<AbstractSpellTimedEvent> eventList = new ArrayList<>();

    public MultiSpellEvent add(AbstractSpellTimedEvent event) {
        eventList.add(event);
        return this;
    }

    @Override
    public void run(Player player) {
        for (AbstractSpellTimedEvent e : eventList) {
            e.run(player);
        }
    }
}
