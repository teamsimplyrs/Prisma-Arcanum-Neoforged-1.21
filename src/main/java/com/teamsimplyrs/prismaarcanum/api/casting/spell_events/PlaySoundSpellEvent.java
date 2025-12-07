package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class PlaySoundSpellEvent extends AbstractSpellTimedEvent {
    private final SoundEvent sound;
    private final float volume, pitch;

    public PlaySoundSpellEvent(SoundEvent sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void run(Player player) {
        if (!player.level().isClientSide()) {
            player.level().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    sound,
                    SoundSource.PLAYERS,
                    volume,
                    pitch
            );
        }
    }
}
