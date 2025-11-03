package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record ManaSyncPayload(UUID playerUUID, PlayerChromana mana) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManaSyncPayload> PAYLOAD_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "mana_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ManaSyncPayload> CODEC =
            CustomPacketPayload.codec(ManaSyncPayload::write, ManaSyncPayload::new);

    public ManaSyncPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID(), readMana(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        writeMana(buf, mana);
    }

    private static void writeMana(FriendlyByteBuf buf, PlayerChromana m) {
        buf.writeVarInt(m.getCurrent());
        buf.writeVarInt(m.getMax());
        buf.writeFloat(m.getRegen());
        buf.writeVarInt(m.getRegenCooldown());
    }

    private static PlayerChromana readMana(RegistryFriendlyByteBuf buf) {
        int cur = buf.readVarInt();
        int max = buf.readVarInt();
        float regen = buf.readFloat();
        int regenCooldown = buf.readVarInt();

        return new PlayerChromana(cur, max, regen, regenCooldown);
    }

    public static void handle(ManaSyncPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof LocalPlayer clientPlayer) {
                var attachmentType = PADataAttachmentsRegistry.CHROMANA.get();
                PlayerChromana mana = clientPlayer.getData(attachmentType);
                mana.setCurrent(payload.mana.getCurrent());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
