package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientFXSpellEvent extends AbstractSpellTimedEvent {
    private final ResourceLocation fxId;

    //UNTESTED
    public ClientFXSpellEvent(ResourceLocation fxId) {
        this.fxId = fxId;
    }

    @Override
    public void run(Player player) {
        if (player.level().isClientSide()) {
            FX fx = FXHelper.getFX(fxId);
            new EntityEffectExecutor(fx, player.level(), player, EntityEffectExecutor.AutoRotate.NONE).start();
        }
    }
}
