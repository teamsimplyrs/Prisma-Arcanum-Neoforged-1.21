package com.teamsimplyrs.prismaarcanum.component;

import com.mojang.serialization.Codec;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class PADataComponents {
    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION_STREAM_CODEC =
            StreamCodec.of(
                    FriendlyByteBuf::writeResourceLocation,
                    FriendlyByteBuf::readResourceLocation
            );

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PrismaArcanum.MOD_ID);

    /*
     * Stores the spell ID (ResourceLocation) for any item that a spell can be bound to.
     * Persistent: saves to NBT in world/loot tables.
     * Network-synchronized: auto-syncs server→client on changes.
     */
    public static final Supplier<DataComponentType<ResourceLocation>> SPELL_ID =
            DATA_COMPONENTS.registerComponentType(
                    "spell_id",
                    builder -> builder
                            .persistent(ResourceLocation.CODEC)
                            .networkSynchronized(RESOURCE_LOCATION_STREAM_CODEC)
            );

    public static final Supplier<DataComponentType<List<ResourceLocation>>> SPELLS_BOUND =
            DATA_COMPONENTS.registerComponentType(
                    "spells_bound",
                    builder -> builder
                            .persistent(Codec.list(ResourceLocation.CODEC))
                            .networkSynchronized(StreamCodec.of(
                                    // write entries
                                    (RegistryFriendlyByteBuf buf, List<ResourceLocation> list) -> {
                                        buf.writeVarInt(list.size());
                                        for (var resLoc: list) {
                                            buf.writeResourceLocation(resLoc);
                                        }
                                    },

                                    // read entries
                                    (RegistryFriendlyByteBuf buf) -> {
                                        int size = buf.readVarInt();
                                        List<ResourceLocation> result = new ArrayList<>(size);
                                        for (int i = 0; i < size; i++) {
                                            result.add(buf.readResourceLocation());
                                        }

                                        return Collections.unmodifiableList(result);
                                    }
                            ))
            );

    public static final Supplier<DataComponentType<Integer>> CURRENT_SPELL_INDEX =
            DATA_COMPONENTS.registerComponentType(
                    "current_spell_index",
                    builder -> builder
                            .persistent(Codec.INT)
                            .networkSynchronized(StreamCodec.of(
                                    FriendlyByteBuf::writeVarInt,
                                    FriendlyByteBuf::readVarInt
                            ))
            );

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
