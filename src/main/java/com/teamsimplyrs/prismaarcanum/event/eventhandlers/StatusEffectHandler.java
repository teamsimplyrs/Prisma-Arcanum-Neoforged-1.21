package com.teamsimplyrs.prismaarcanum.event.eventhandlers;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.common.AbstractStatusEffect;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
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

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT)
class CameraShakeHandler {
    // Smooth random offset to prevent jittering noise each frame
    private static float shakeX = 0;
    private static float shakeY = 0;
    private static float targetX = 0;
    private static float targetY = 0;

    // how fast the shake interpolates to new values (0–1)
    private static final float SMOOTHING = 0.6f;

    @SubscribeEvent
    public static void onCameraShake(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        //if(!mc.isPaused()) {
            if (mc.player == null  || mc.isPaused()) return;

            var effect = mc.player.getEffect(
                    PASpellEffectRegistry.ZAPPED
            );
            if (effect == null) {
                // ease back to zero when effect ends
                targetX = 0;
                targetY = 0;
                smoothShake();
                event.setPitch(event.getPitch() + shakeX);
                event.setYaw(event.getYaw() + shakeY);
                return;
            }

            // intensity based on amplifier (and maybe duration)
            float intensity = 1.5f + effect.getAmplifier();  // tweak for strength

            // every few ticks, pick a new random shake target
            if (mc.player.tickCount % 3 == 0) {
                targetX = (mc.level.random.nextFloat() - 0.5f) * intensity;
                targetY = (mc.level.random.nextFloat() - 0.5f) * intensity;
            }

            smoothShake();
            // Apply shake to camera angles
            event.setPitch(event.getPitch() + shakeX);
            event.setYaw(event.getYaw() + shakeY);
      //  }
    }

    private static void smoothShake() {
        shakeX += (targetX - shakeX) * SMOOTHING;
        shakeY += (targetY - shakeY) * SMOOTHING;
    }
}
