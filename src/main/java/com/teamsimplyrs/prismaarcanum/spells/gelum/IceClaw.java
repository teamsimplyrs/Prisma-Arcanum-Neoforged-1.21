package com.teamsimplyrs.prismaarcanum.spells.gelum;

import com.lowdragmc.photon.client.fx.BlockEffectExecutor;
import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.PhysicsUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpellEffectAreaEntity;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class IceClaw extends AbstractSpell {

    public static final String SPELL_ID = "ice_claw";

    public IceClaw() {
        super(Element.Gelum, School.Mortality, 1, 15, 50, 0, true);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (world.isClientSide) {
            return;
        }

        float range = 7f;

        Vec3 spawnPos = PhysicsUtils.raycastForPosition(player, world, range);

        SpellEffectAreaEntity claw = PAEntityRegistry.SPELL_EFFECT_AREA.get().create(world);

        claw.setPos(spawnPos);
        claw.configure(
                getResourceLocation(),
                getLifetime(),
                2f,
                2f
        );
        claw.setFxID(getFXid());

        world.addFreshEntity(claw);
    }

    @Override
    public void hitboxTick(SpellEffectAreaEntity hitbox) {
        super.hitboxTick(hitbox);

        if (hitbox.level().isClientSide) {
            if (!hitbox.particleEmitted) {
                FX fx = FXHelper.getFX(getFXid());
                if (fx != null) {
                    hitbox.particleEmitted = true;
                    EntityEffectExecutor fxExec = new EntityEffectExecutor(fx, hitbox.level(), hitbox, EntityEffectExecutor.AutoRotate.NONE);
                    fxExec.start();
                }
            }
        }

        AABB box = hitbox.getBoundingBox();

        for (Entity e : hitbox.level().getEntities(hitbox, box)) {
            if (e instanceof LivingEntity living && living.isAlive()) {
                e.hurt(hitbox.damageSources().magic(), 8f);
            }
        }
    }

    @Override
    public int getLifetime() {
        return 20;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, SPELL_ID);
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "ice_claw_fx");
    }
}
