package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.common.AbstractStatusEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.slf4j.Logger;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT)
public class StatusEffectHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void addEffect(MobEffectEvent.Added event) {
        if(event.getEffectInstance().getEffect().value() instanceof AbstractStatusEffect effect) {
            event.getEffectInstance().visible = false;
            //effect.renderFX(event.getEntity().level(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void effectCured(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        for (var instance : entity.getActiveEffects()) {
            if (instance.getEffect().value() instanceof AbstractStatusEffect effect) {
                effect.removeFX(entity);
            }
        }
    }

    @SubscribeEvent
    public static void effectExpired(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        for (var instance : entity.getActiveEffects()) {
            if (instance.getEffect().value() instanceof AbstractStatusEffect effect) {
                effect.removeFX(entity);
            }
        }
    }
}
