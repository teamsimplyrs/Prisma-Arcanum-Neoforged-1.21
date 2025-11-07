package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.network.payload.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkEventsHandler {
    @SubscribeEvent
    public static void registerNetworkPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PrismaArcanum.MOD_ID);

        PlayToServer(registrar);
        PlayToClient(registrar);
        PlayBidirectional(registrar);
    }

    private static void PlayToServer(PayloadRegistrar registrar) {
        registrar.playToServer(CastPayload.PAYLOAD_TYPE, CastPayload.CODEC, CastPayload::handle);
        registrar.playToServer(OnPrismConvergePayload.PAYLOAD_TYPE, OnPrismConvergePayload.CODEC, OnPrismConvergePayload::handle);
        registrar.playToServer(OnSelectedSpellChangedPayload.PAYLOAD_TYPE, OnSelectedSpellChangedPayload.CODEC, OnSelectedSpellChangedPayload::handle);
    }

    private static void PlayToClient(PayloadRegistrar registrar) {
        registrar.playToClient(OnCastingStartedPayload.PAYLOAD_TYPE, OnCastingStartedPayload.CODEC, OnCastingStartedPayload::handle);
        registrar.playToClient(OnCastingFinishedPayload.PAYLOAD_TYPE, OnCastingFinishedPayload.CODEC, OnCastingFinishedPayload::handle);
        registrar.playToClient(OnCustomProjectileSpawnedPayload.PAYLOAD_TYPE, OnCustomProjectileSpawnedPayload.CODEC, OnCustomProjectileSpawnedPayload::handle);
        registrar.playToClient(ManaSyncPayload.PAYLOAD_TYPE, ManaSyncPayload.CODEC, ManaSyncPayload::handle);
        registrar.playToClient(PlayerSpellCooldownsSyncPayload.PAYLOAD_TYPE, PlayerSpellCooldownsSyncPayload.CODEC, PlayerSpellCooldownsSyncPayload::handle);
    }

    private static void PlayBidirectional(PayloadRegistrar registrar) {

    }
}
