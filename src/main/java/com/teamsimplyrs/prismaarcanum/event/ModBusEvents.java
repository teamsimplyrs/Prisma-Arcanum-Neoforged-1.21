package com.teamsimplyrs.prismaarcanum.event;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.FireballSpellProjectileModel;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.ManaPelletModel;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.entity.client.monster.RippleSeekerModel;
import com.teamsimplyrs.prismaarcanum.entity.client.projectile.RippleSeekerProjectileModel;
import com.teamsimplyrs.prismaarcanum.entity.custom.monster.RippleSeekerEntity;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ManaPelletModel.LAYER_LOCATION, ManaPelletModel::createBodyLayer);
        event.registerLayerDefinition(FireballSpellProjectileModel.LAYER_LOCATION, FireballSpellProjectileModel::createBodyLayer);
        event.registerLayerDefinition(RippleSeekerModel.LAYER_LOCATION, RippleSeekerModel::createBodyLayer);
        event.registerLayerDefinition(RippleSeekerProjectileModel.LAYER_LOCATION, RippleSeekerProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(PAEntityRegistry.RIPPLE_SEEKER.get(), RippleSeekerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(SpellRegistry.SPELL_REGISTRY);
    }
}
