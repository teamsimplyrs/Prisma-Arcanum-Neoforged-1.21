package com.teamsimplyrs.prismaarcanum.api.status_effect.ignis;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.common.AbstractStatusEffect;
import com.teamsimplyrs.prismaarcanum.network.payload.OnStatusEffectAppliedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class NapalmBurnEffect extends AbstractStatusEffect {

    public NapalmBurnEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF4500);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return true; // or your custom condition
    }

    /** Called when a tick should deal damage */
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (!level.isClientSide) {
            // Wither-like magical damage that bypasses armor and CAN kill
            entity.hurt(entity.damageSources().magic(), 0.5F + (amplifier * 0.3F));
        }
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        Level level = livingEntity.level();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                livingEntity,
                new OnStatusEffectAppliedPayload(
                        livingEntity.getId(),
                        ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "napalm_burn")
                )
        );
    }

    @Override
    public ResourceLocation getFX() {
        return ResourceLocation.fromNamespaceAndPath(
                PrismaArcanum.MOD_ID, "napalm_burn_effect"
        );
    }
}
