package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

//Only for testing, needs to be modified.
public class ServerSpellEvent extends AbstractSpellTimedEvent {
    String text;
    public ServerSpellEvent() {
        this.text = "";
    }

    public ServerSpellEvent(String text) {
        this.text = text;
    }

    @Override
    public void run(Player player) {
        if(player instanceof LocalPlayer) return;
        LOGGER.info("Custom event class test "+text);
    }
}
