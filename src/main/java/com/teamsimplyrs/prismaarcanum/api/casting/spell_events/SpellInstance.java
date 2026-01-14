package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import java.util.UUID;

public class SpellInstance {
    public int lifetime;
    public final UUID id;

    public SpellInstance() {
        this.lifetime = 0;
        this.id = UUID.randomUUID();
    }

    public SpellInstance(int lifetime, UUID id) {
        this.lifetime = lifetime;
        this.id = id;
    }
}