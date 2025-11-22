package com.teamsimplyrs.prismaarcanum.network.payload;

import com.lowdragmc.photon.client.fx.BlockEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public record OnFXAtPositionPayload(BlockPos pos, ResourceLocation fxId)
        implements CustomPacketPayload {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Type<OnFXAtPositionPayload> PAYLOAD_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID, "fx_at_position"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, OnFXAtPositionPayload> CODEC =
            CustomPacketPayload.codec(OnFXAtPositionPayload::write, OnFXAtPositionPayload::new);

    public OnFXAtPositionPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeResourceLocation(fxId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }

    public static void handle(OnFXAtPositionPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            FX fx = FXHelper.getFX(payload.fxId);
            if (fx == null) {
                LOGGER.warn(" Unknown FX ID received: {}", payload.fxId());
                return;
            }
            LOGGER.info("network called");
            new BlockEffectExecutor(fx, level, payload.pos()).start();
        });
    }
}