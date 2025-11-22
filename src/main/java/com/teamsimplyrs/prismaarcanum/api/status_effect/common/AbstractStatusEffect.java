package com.teamsimplyrs.prismaarcanum.api.status_effect.common;


import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.util.List;

public abstract class AbstractStatusEffect extends MobEffect {
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected AbstractStatusEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public abstract ResourceLocation getFX();

    public Vector3f getFXOffset(LivingEntity entity) {
        return new Vector3f(0f, entity.getBbHeight() / 2f, 0f);
    }

    public void renderFX(Level level, LivingEntity entity) {
        ResourceLocation fxId = getFX();
        if (fxId == null) return;
        FX fx = FXHelper.getFX(fxId);
        EntityEffectExecutor exec = new EntityEffectExecutor(fx, level, entity, EntityEffectExecutor.AutoRotate.NONE);
        exec.setOffset(getFXOffset(entity));
        exec.start();
    }

    public void removeFX(LivingEntity entity) {

        ResourceLocation fxId = getFX();
        if (fxId == null) return;
        var CACHE = EntityEffectExecutor.CACHE;
        List<EntityEffectExecutor> effects = CACHE.get(entity);
        if (effects == null || effects.isEmpty()) {
            return;
        }
        var iterator = effects.iterator();

        while(iterator.hasNext()) {
            EntityEffectExecutor exec = iterator.next();
            //Not sure if getFxLocation and getRuntime would become null or not
            if(exec.fx.getFxLocation().equals(fxId)){
                var runtime = exec.getRuntime();
                runtime.destroy(true);
                iterator.remove();
            }
        }
        if ((CACHE.get(entity)).isEmpty()) {
            CACHE.remove(entity);
        }

        EntityEffectExecutor.CACHE = CACHE;
    }
}
