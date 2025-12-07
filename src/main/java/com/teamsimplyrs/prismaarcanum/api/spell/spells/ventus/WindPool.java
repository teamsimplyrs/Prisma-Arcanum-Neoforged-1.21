package com.teamsimplyrs.prismaarcanum.api.spell.spells.ventus;

import com.lowdragmc.photon.client.fx.BlockEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpellEffectAreaEntity;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.WindPoolBlankProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class WindPool extends AbstractSpell {
    public static final String spellID = "windpool_spell";
    public static final Element element = Element.Ventus;
    public static final School school = School.Motion;

    public static final int tier = 1;
    public static final int basicManaCost = 0;
    public static final int basicCooldown = 1;
    public static final int spellDelay = 40;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 50f;
    private static final float baseSpeed = 2f;

    public WindPool(){ super(spellID,element,school,tier,basicManaCost,basicCooldown, spellDelay,hasEvolution); }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            super.cast(player, world);

            Vec3 forward = player.getLookAngle().normalize();

            double offsetY = player.getEyeHeight();
            Vec3 spawnPos = player.position()
                    .add(forward.scale(1.0))
                    .add(0, offsetY - 0.25, 0);

            WindPoolBlankProjectile projectile =
                    new WindPoolBlankProjectile(player, world, this.getResourceLocation());

            projectile.setPos(spawnPos);
            projectile.setYRot(player.getYRot());

            projectile.setDeltaMovement(forward.scale(1.0));

            world.addFreshEntity(projectile);

            PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                    projectile,
                    new OnCustomProjectileSpawnedPayload(projectile.getId())
            );
        }
    }

    @Override
    public void hitboxTick(SpellEffectAreaEntity hitbox) {

        if (hitbox.level().isClientSide) {
            if (!hitbox.particleEmitted) {
                FX fx = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(
                        PrismaArcanum.MOD_ID, "sucker"));
                hitbox.particleEmitted = true;
                new BlockEffectExecutor(fx, hitbox.level(), hitbox.blockPosition()).start();
            }
            return;
        }

        AABB box = hitbox.getBoundingBox();

        for (Entity e : hitbox.level().getEntities(hitbox, box)) {
            if (e instanceof LivingEntity living && living.isAlive()) {

                Vec3 center = hitbox.position();
                Vec3 dirToCenter = center.subtract(living.position()).normalize();

                double pullStrength = 0.2D;

                Vec3 newMotion = living.getDeltaMovement().add(dirToCenter.scale(pullStrength));
                living.setDeltaMovement(newMotion);

                living.fallDistance = 0F;
            }
        }
    }

    @Override
    public ResourceLocation getAnimationLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID,"cast_simple");
    }
}
