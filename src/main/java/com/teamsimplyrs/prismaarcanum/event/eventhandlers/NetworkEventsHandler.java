package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.network.payload.CastPayload;
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
    }

    private static void PlayToClient(PayloadRegistrar registrar) {

    }

    private static void PlayBidirectional(PayloadRegistrar registrar) {

    }
}
