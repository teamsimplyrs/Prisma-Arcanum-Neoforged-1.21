package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public abstract class AbstractSpellTimedEvent {
    protected final Logger LOGGER = LogUtils.getLogger();
    public abstract void run(Player player);
}

