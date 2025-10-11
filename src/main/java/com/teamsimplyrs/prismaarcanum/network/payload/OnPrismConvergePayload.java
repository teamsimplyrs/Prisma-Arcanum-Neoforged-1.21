package com.teamsimplyrs.prismaarcanum.network.payload;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.utils.PlayerUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.SpellUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import com.teamsimplyrs.prismaarcanum.client.menu.container.PrismaFocusBenchMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record OnPrismConvergePayload(int castableItemSlotIndex, List<ResourceLocation> spellIDs) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OnPrismConvergePayload> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "on_prism_converge"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnPrismConvergePayload> CODEC =
            CustomPacketPayload.codec(OnPrismConvergePayload::write, OnPrismConvergePayload::new);

    public OnPrismConvergePayload(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), readSpellList(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(castableItemSlotIndex);
        buf.writeVarInt(spellIDs.size());
        for (var id: spellIDs) {
            buf.writeResourceLocation(id);
        }
    }

    private static List<ResourceLocation> readSpellList(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<ResourceLocation> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readResourceLocation());
        }
        return List.copyOf(list);
    }

    public static void handle(OnPrismConvergePayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) ctx.player();
            if (sender == null) {
                return;
            }

            if (sender.containerMenu instanceof PrismaFocusBenchMenu focusBenchMenu) {
                ItemStack stack  = focusBenchMenu.getSlot(payload.castableItemSlotIndex).getItem();
                if (stack.getItem() instanceof AbstractCastable castable) {
                    SpellUtils.writeSpellsToCastableItem(stack, payload.spellIDs);
                    WandUtils.resetCurrentSpellIndex(stack, true);
                    PlayerUtils.moveItemStackToPlayerInventory(sender, stack, focusBenchMenu, payload.castableItemSlotIndex);
                    focusBenchMenu.broadcastChanges();
                    focusBenchMenu.blockEntity.setChanged();
                    sender.getInventory().setChanged();
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}
