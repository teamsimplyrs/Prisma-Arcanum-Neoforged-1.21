package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.SpellLifetimeTracker;
import com.teamsimplyrs.prismaarcanum.api.casting.spell_events.SpellInstance;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.*;

public record SpellLifetimeSyncPayload(
        Map<ResourceLocation, List<SpellInstance>> lifetimes
) implements CustomPacketPayload {

    public static final Type<SpellLifetimeSyncPayload> PAYLOAD_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID,
                    "player_spell_lifetime_sync"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellLifetimeSyncPayload> CODEC =
            CustomPacketPayload.codec(SpellLifetimeSyncPayload::write, SpellLifetimeSyncPayload::new);

    public SpellLifetimeSyncPayload(RegistryFriendlyByteBuf buf) {
        this(readMap(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(lifetimes.size());

        for (var entry : lifetimes.entrySet()) {
            buf.writeResourceLocation(entry.getKey());

            List<SpellInstance> instances = entry.getValue();
            buf.writeVarInt(instances.size());

            for (SpellInstance instance : instances) {
                buf.writeUUID(instance.id);
                buf.writeVarInt(instance.lifetime);
            }
        }
    }

    private static Map<ResourceLocation, List<SpellInstance>> readMap(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<ResourceLocation, List<SpellInstance>> map = new HashMap<>(size);

        for (int i = 0; i < size; i++) {
            ResourceLocation spellId = buf.readResourceLocation();
            int count = buf.readVarInt();

            List<SpellInstance> instances = new ArrayList<>(count);

            for (int j = 0; j < count; j++) {
                UUID id = buf.readUUID();
                int lifetime = buf.readVarInt();
                instances.add(new SpellInstance(lifetime, id));
            }

            map.put(spellId, instances);
        }

        return map;
    }

    public static void handle(SpellLifetimeSyncPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof LocalPlayer clientPlayer) {
                SpellLifetimeTracker tracker =
                        clientPlayer.getData(PADataAttachmentsRegistry.SPELL_LIFETIMES.get());

                tracker.setAllLifetimes(payload.lifetimes);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}