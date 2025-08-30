package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record OnCastingStartedPayload(UUID uuid, ResourceLocation spellID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OnCastingStartedPayload> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "on_casting_started"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnCastingStartedPayload> CODEC =
            CustomPacketPayload.codec(OnCastingStartedPayload::write, OnCastingStartedPayload::new);

    public OnCastingStartedPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeResourceLocation(spellID);
    }

    public static void handle(OnCastingStartedPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            ResourceLocation spellID = payload.spellID;
            Level world = player.level();

        });
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
