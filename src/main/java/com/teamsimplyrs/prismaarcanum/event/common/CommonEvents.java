package com.teamsimplyrs.prismaarcanum.event.common;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateComponent;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import com.teamsimplyrs.prismaarcanum.network.payload.ManaSyncPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.PlayerSpellCooldownsSyncPayload;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer) {
            PlayerChromana manaData = serverPlayer.getData(PADataAttachmentsRegistry.CHROMANA.get());
            PlayerSpellCooldowns spellCooldowns = serverPlayer.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());

            if (manaData.tick()) {
                if (serverPlayer.tickCount % 5 == 0  || manaData.getCurrent() == manaData.getMax()) {
                    PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(serverPlayer.getUUID(), manaData));
                }
            }

            if (spellCooldowns.tick()) {
                if (serverPlayer.tickCount % 50 == 0 || spellCooldowns.isMarkedDirty()) {
                    spellCooldowns.unmarkDirty();
                    PacketDistributor.sendToPlayer(serverPlayer, new PlayerSpellCooldownsSyncPayload(spellCooldowns.getCooldownMap()));
                }
            }
        } else if (entity instanceof LocalPlayer localPlayer) {
            PlayerChromana manaData = localPlayer.getData(PADataAttachmentsRegistry.CHROMANA.get());
            PlayerSpellCooldowns spellCooldowns = localPlayer.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());

            if (manaData.tick()) {

            }

            if (spellCooldowns.tick()) {

            }
        }
    }


    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!entity.level().isClientSide) {
            if (entity instanceof LivingEntity livingEntity) {
                EntitySpellControlStateComponent spellControlStateData = livingEntity.getData(PADataAttachmentsRegistry.SPELL_CONTROL_STATE.get());
                spellControlStateData.tick(entity);
            }
        }
    }
}
