package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.ClientCooldownManager;
import com.teamsimplyrs.prismaarcanum.api.casting.SpellLifetimeTracker;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record SpellLifetimeSyncPayload(Map<ResourceLocation, Integer> lifetimes) implements CustomPacketPayload {
    public static final Type<SpellLifetimeSyncPayload> PAYLOAD_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "player_spell_lifetime_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellLifetimeSyncPayload> CODEC =
            CustomPacketPayload.codec(SpellLifetimeSyncPayload::write, SpellLifetimeSyncPayload::new);

    public SpellLifetimeSyncPayload(RegistryFriendlyByteBuf buf) {
        this(readMap(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(lifetimes.size());
        for (Map.Entry<ResourceLocation, Integer> e : lifetimes.entrySet()) {
            buf.writeResourceLocation(e.getKey());
            buf.writeVarInt(e.getValue());
        }
    }

    private static Map<ResourceLocation, Integer> readMap(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<ResourceLocation, Integer> lifetimes = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation spellID = buf.readResourceLocation();
            int ticks = buf.readVarInt();
            lifetimes.put(spellID, ticks);
        }
        return lifetimes;
    }

    public static void handle(SpellLifetimeSyncPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof LocalPlayer clientPlayer) {
                SpellLifetimeTracker lifetimes = clientPlayer.getData(PADataAttachmentsRegistry.SPELL_LIFETIMES.get());
                lifetimes.setAllLifetimes(payload.lifetimes);
                ClientCooldownManager.get().setCooldowns(payload.lifetimes);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
