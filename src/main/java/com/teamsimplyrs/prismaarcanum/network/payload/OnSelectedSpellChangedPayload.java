package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record OnSelectedSpellChangedPayload(int newIndex) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OnSelectedSpellChangedPayload> PAYLOAD_TYPE =
            new CustomPacketPayload.Type<OnSelectedSpellChangedPayload>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "on_selected_spell_changed"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OnSelectedSpellChangedPayload> CODEC =
            CustomPacketPayload.codec(OnSelectedSpellChangedPayload::write, OnSelectedSpellChangedPayload::new);


    public OnSelectedSpellChangedPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(newIndex);
    }

    public static void handle(OnSelectedSpellChangedPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            ItemStack handItem = player.getMainHandItem();
            if (handItem.getItem() instanceof AbstractCastable castable) {
                List<ResourceLocation> spellsBound = handItem.get(PADataComponents.SPELLS_BOUND.get());
                int resultIndex = (spellsBound == null || spellsBound.isEmpty()) ?
                        -1 : (payload.newIndex >= spellsBound.size()) ?
                            0 : payload.newIndex;
                handItem.set(PADataComponents.CURRENT_SPELL_INDEX, resultIndex);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
