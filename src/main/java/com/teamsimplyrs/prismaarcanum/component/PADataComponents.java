package com.teamsimplyrs.prismaarcanum.component;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class PADataComponents {
    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION_STREAM_CODEC =
            StreamCodec.of(
                    FriendlyByteBuf::writeResourceLocation,
                    FriendlyByteBuf::readResourceLocation
            );

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PrismaArcanum.MOD_ID);

    /**
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

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
