package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OnCustomProjectileSpawnedPayload(int projectileID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OnCustomProjectileSpawnedPayload> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "on_custom_projectile_spawned"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnCustomProjectileSpawnedPayload> CODEC =
            CustomPacketPayload.codec(OnCustomProjectileSpawnedPayload::write, OnCustomProjectileSpawnedPayload::new);

    public OnCustomProjectileSpawnedPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(projectileID);
    }

    public static void handle(OnCustomProjectileSpawnedPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(payload.projectileID);
            if (entity instanceof AbstractSpellProjectile spellProjectile) {
                spellProjectile.startTrailFX();
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
