package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record OnCastingFinishedPayload(UUID uuid, ResourceLocation spellID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OnCastingFinishedPayload> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "on_casting_finished"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnCastingFinishedPayload> CODEC =
            CustomPacketPayload.codec(OnCastingFinishedPayload::write, OnCastingFinishedPayload::new);

    public OnCastingFinishedPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeResourceLocation(spellID);
    }

    public static void handle(OnCastingFinishedPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            AbstractSpell spell = SpellRegistry.getSpell(payload.spellID);
            Level world = player.level();
            Player caster = world.getPlayerByUUID(payload.uuid);
            spell.onCastingFinished(caster, world);
        });
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
