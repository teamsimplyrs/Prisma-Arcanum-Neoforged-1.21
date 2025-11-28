package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.item.WandItemExtensions;
import com.teamsimplyrs.prismaarcanum.registry.PAItemRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelHandler {
    @SubscribeEvent
    public static void registerModelsEvent(ModelEvent.RegisterAdditional event){
        registerModel("item/wand_staff",event);
        registerModel("item/gem_staff",event);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(new WandItemExtensions(), PAItemRegistry.DEBUG_WAND.get());
    }

    public static void registerModel(String location,ModelEvent.RegisterAdditional event){
        ModelResourceLocation modelLocation = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID,location));
        event.register(modelLocation);
    }
}
