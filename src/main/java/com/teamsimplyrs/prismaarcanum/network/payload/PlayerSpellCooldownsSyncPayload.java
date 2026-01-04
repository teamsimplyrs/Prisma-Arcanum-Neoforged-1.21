package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
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

public record PlayerSpellCooldownsSyncPayload(Map<ResourceLocation, Integer> cooldowns) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerSpellCooldownsSyncPayload> PAYLOAD_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "player_spell_cooldowns_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpellCooldownsSyncPayload> CODEC =
            CustomPacketPayload.codec(PlayerSpellCooldownsSyncPayload::write, PlayerSpellCooldownsSyncPayload::new);

    public PlayerSpellCooldownsSyncPayload(RegistryFriendlyByteBuf buf) {
        this(readMap(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(cooldowns.size());
        for (Map.Entry<ResourceLocation, Integer> e : cooldowns.entrySet()) {
            buf.writeResourceLocation(e.getKey());
            buf.writeVarInt(e.getValue());
        }
    }

    private static Map<ResourceLocation, Integer> readMap(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<ResourceLocation, Integer> cooldowns = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ResourceLocation spellID = buf.readResourceLocation();
            int ticks = buf.readVarInt();
            cooldowns.put(spellID, ticks);
        }
        return cooldowns;
    }

    public static void handle(PlayerSpellCooldownsSyncPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof LocalPlayer clientPlayer) {
                PlayerSpellCooldowns cooldowns = clientPlayer.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());
                cooldowns.setCooldowns(payload.cooldowns);
                clientPlayer.setData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get(), cooldowns);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
