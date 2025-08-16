package com.teamsimplyrs.prismaarcanum.system.events;

import net.neoforged.bus.api.Event;

public class SpellsReloadedEvent extends Event {
    private final int newSpellsTotal;
    public SpellsReloadedEvent(int newSpellsTotal) {
        this.newSpellsTotal = newSpellsTotal;
    }

    public int getCount() {
        return newSpellsTotal;
    }
}
