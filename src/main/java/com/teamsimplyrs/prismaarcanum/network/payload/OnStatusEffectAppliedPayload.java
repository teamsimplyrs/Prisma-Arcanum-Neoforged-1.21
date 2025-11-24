package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.common.AbstractStatusEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OnStatusEffectAppliedPayload(int entityId, ResourceLocation effectId)
        implements CustomPacketPayload {

    public static final Type<OnStatusEffectAppliedPayload> PAYLOAD_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "on_status_effect_applied"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OnStatusEffectAppliedPayload> CODEC =
            CustomPacketPayload.codec(OnStatusEffectAppliedPayload::write, OnStatusEffectAppliedPayload::new);

    public OnStatusEffectAppliedPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeResourceLocation(effectId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }

    public static void handle(OnStatusEffectAppliedPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Entity e = Minecraft.getInstance().level.getEntity(payload.entityId);

            AbstractStatusEffect effect = (AbstractStatusEffect) BuiltInRegistries.MOB_EFFECT.get(payload.effectId);
            effect.renderFX(e.level(), (LivingEntity) e);
        });
    }
}