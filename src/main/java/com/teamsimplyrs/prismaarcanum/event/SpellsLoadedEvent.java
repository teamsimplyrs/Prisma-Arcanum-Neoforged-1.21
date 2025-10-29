package com.teamsimplyrs.prismaarcanum.event;

import net.neoforged.bus.api.Event;

public class SpellsLoadedEvent extends Event {
    private final int spellsTotal;
    public SpellsLoadedEvent(int newSpellsTotal) {
        this.spellsTotal = newSpellsTotal;
    }

    public int getCount() {
        return spellsTotal;
    }
}
