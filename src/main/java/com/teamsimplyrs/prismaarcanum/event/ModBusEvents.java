package com.teamsimplyrs.prismaarcanum.event;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.ignis.FireballSpell;
import com.teamsimplyrs.prismaarcanum.entity.client.FireballSpellProjectileModel;
import com.teamsimplyrs.prismaarcanum.entity.client.ManaPelletModel;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ManaPelletModel.LAYER_LOCATION, ManaPelletModel::createBodyLayer);
        event.registerLayerDefinition(FireballSpellProjectileModel.LAYER_LOCATION, FireballSpellProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(SpellRegistry.SPELL_REGISTRY);
    }
}
