package com.teamsimplyrs.prismaarcanum.api.status_effect.ignis;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.status_effect.common.AbstractStatusEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class NapalmBurnEffect extends AbstractStatusEffect {

    public NapalmBurnEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF4500);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (!level.isClientSide) {
            entity.hurt(entity.damageSources().inFire(), 1.0F + amplifier);
        }
        return true;
    }

    @Override
    public ResourceLocation getFX() {
        return ResourceLocation.fromNamespaceAndPath(
                PrismaArcanum.MOD_ID, "napalm_burn_effect"
        );
    }
}
