package com.teamsimplyrs.prismaarcanum.spells.gelum;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.combo.SpellComboChainData;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.PhysicsUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpellEffectAreaEntity;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Quaternionf;

public class IceClaw extends AbstractSpell {

    public static final String SPELL_ID = "ice_claw";

    public IceClaw() {
        super(Element.Gelum, School.Assassination, 1, 15, 70, 0, true);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (world.isClientSide) {
            return;
        }

        SpellComboChainData comboChain = player.getData(PADataAttachmentsRegistry.SPELL_COMBO_CHAIN_DATA.get());

        float damage = comboChain.getCurrentComboCount() * 3 + 1;
        float range = 7f;

        SpellEffectAreaEntity claw = PAEntityRegistry.SPELL_EFFECT_AREA.get().create(world);
        Vec3 spawnPos = PhysicsUtils.raycastForPosition(player, world, range, new Vec3(0, -0.5, 0));


        claw.setPos(spawnPos);
        claw.configure(
                getResourceLocation(),
                getLifetime(),
                2f,
                2f,
                damage
        );
        claw.setFxID(getFXid());

        world.addFreshEntity(claw);

        super.cast(player, world);
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

                    var randSrc = RandomSource.create();
                    int zRot = Mth.randomBetweenInclusive(randSrc, -45, 20); // TO DO: add fixed rotations based on combo counter.


                    fxExec.start();
                    fxExec.setRotation(0, 0, zRot);
                }
            }
        }

        AABB box = hitbox.getBoundingBox();

        for (Entity e : hitbox.level().getEntities(hitbox, box)) {
            if (e instanceof LivingEntity living && living.isAlive()) {
                e.hurt(hitbox.damageSources().magic(), hitbox.damage);
            }
        }
    }

    @Override
    public int getLifetime() {
        return 15;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, SPELL_ID);
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "ice_claw_fx");
    }

    @Override
    public boolean hasCombo() {
        return true;
    }

    @Override
    public int getMaxComboCount() {
        return tier + 2;
    }

    @Override
    public int getInternalComboCooldownTicks() {
        return 10 - (tier / 2 + 1);
    }

    @Override
    public int getComboWindowTicks() {
        return 20;
    }
}
