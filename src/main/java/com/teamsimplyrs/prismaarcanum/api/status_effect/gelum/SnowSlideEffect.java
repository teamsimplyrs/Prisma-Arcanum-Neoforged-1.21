package com.teamsimplyrs.prismaarcanum.api.status_effect.gelum;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.common.AbstractStatusEffect;
import com.teamsimplyrs.prismaarcanum.network.payload.OnStatusEffectAppliedPayload;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class SnowSlideEffect extends AbstractStatusEffect {

    public SnowSlideEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x36d1c9);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        Level level = livingEntity.level();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                livingEntity,
                new OnStatusEffectAppliedPayload(
                        livingEntity.getId(),
                        ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_effect")
                )
        );
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        super.onEffectStarted(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration >= 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.isSprinting()) {
            Vec3 motion = livingEntity.getDeltaMovement();

            double retain = 0.96 + (amplifier * 0.01); // tweakable
            double boost = 1.02 + (amplifier * 0.01);

            livingEntity.setDeltaMovement(
                    motion.x * retain * boost,
                    motion.y,
                    motion.z * retain * boost
            );

            livingEntity.hurtMarked = true;
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public ResourceLocation getFX() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_fx");
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        var step = attributeMap.getInstance(Attributes.STEP_HEIGHT);
        var speed = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (step != null) {
            step.removeModifier(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_step_height"));
        }

        if (speed != null) {
            speed.removeModifier(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_movement_speed"));
        }
    }

    @EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class Events {
        @SubscribeEvent
        public static void onEffectAdded(MobEffectEvent.Added event) {
            if (event.getEffectInstance().is(PASpellEffectRegistry.SNOW_SLIDE_EFFECT)) {
                var entity = event.getEntity();
                var stepAttr = entity.getAttribute(Attributes.STEP_HEIGHT);
                var speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);

                if (stepAttr != null) {
                    stepAttr.addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_step_height"), 0.4f, AttributeModifier.Operation.ADD_VALUE));
                }

                if (speedAttr != null) {
                    speedAttr.addTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_movement_speed"), 1.3f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                }
            }
        }

        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {

        }
    }
}
