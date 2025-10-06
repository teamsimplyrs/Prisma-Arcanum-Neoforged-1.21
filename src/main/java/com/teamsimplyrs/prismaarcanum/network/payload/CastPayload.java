package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record CastPayload(UUID uuid, ResourceLocation spellID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CastPayload> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "cast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CastPayload> CODEC =
            CustomPacketPayload.codec(CastPayload::write, CastPayload::new);

    public CastPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeResourceLocation(spellID);
    }

    public static void handle(CastPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            ResourceLocation spellID = payload.spellID;
            Level world = player.level();
            if (player instanceof ServerPlayer) {
                AbstractSpell spell = SpellRegistry.getSpell(spellID);
                spell.cast((ServerPlayer)player, world);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }


}
