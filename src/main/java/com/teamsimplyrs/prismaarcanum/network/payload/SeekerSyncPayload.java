package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SeekerSyncPayload(int entityId, boolean attack1Scheduled, int attack1Delay)
        implements CustomPacketPayload {

    public static final Type<SeekerSyncPayload> PAYLOAD_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID, "seeker_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SeekerSyncPayload> CODEC =
            CustomPacketPayload.codec(SeekerSyncPayload::write, SeekerSyncPayload::new);

    public SeekerSyncPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(),
                buf.readBoolean(),
                buf.readVarInt()
        );
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeBoolean(attack1Scheduled);
        buf.writeVarInt(attack1Delay);
    }

    public static void handle(SeekerSyncPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Entity e = Minecraft.getInstance().level.getEntity(payload.entityId);
            if (e instanceof RippleSeekerEntity seeker) {
                seeker.setAttackSync(payload.attack1Scheduled,payload.attack1Delay);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
