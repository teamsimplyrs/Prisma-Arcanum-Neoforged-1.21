package com.teamsimplyrs.prismaarcanum.api.spell.spells.ignis;

import com.lowdragmc.photon.client.fx.BlockEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.NapalmShootBlankProjectile;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpellEffectAreaEntity;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class NapalmSpraySpell extends AbstractSpell {

    public static final String spellID = "napalm_spray_spell";
    public static final Element element = Element.Ignis;
    public static final School school = School.Annihilation;

    public static final int tier = 1;
    public static final int basicManaCost = 0;
    public static final int basicCooldown = 1;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 50f;
    private static final float baseSpeed = 2f;

    public NapalmSpraySpell() {
        super(spellID,element,school,tier, basicManaCost,basicCooldown,hasEvolution);
    }

    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            super.cast(player, world);

            Vec3 forward = player.getLookAngle().normalize();
            Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();
            double baseY = player.getEyeHeight();
            int count = 10;
            double arcRadius = 18.0;
            double depthRadius = 3.0;
            double baseSpeed = 0.6;

            for (int i = 0; i < count; i++) {
                NapalmShootBlankProjectile projectile = new NapalmShootBlankProjectile(player, world, this.getResourceLocation());

                double angleRange = Math.PI / 4;
                double angle = -angleRange / 2 + angleRange * (i / (double)(count - 1));

                Vec3 offset = forward.scale(Math.cos(angle) * depthRadius)
                        .add(right.scale(Math.sin(angle) * arcRadius * 0.5));

                // Vertical separation so that there's a delay going from left to right when summoning particles.
                double verticalOffset = baseY - (projectile.getBoundingBox().getYsize() / 2.0) + (i * 0.15);

                Vec3 spawnPos = player.position().add(offset).add(0, verticalOffset, 0);
                projectile.setPos(spawnPos);
                projectile.setYRot(player.getYRot());

                double progress = (double) i / (count - 1);
                double downward = -0.06 * (1.0 - progress * 0.3);
                double speed = baseSpeed * (1.0 + progress * 0.1);

                Vec3 tangentDir = forward.scale(Math.cos(angle)).add(right.scale(Math.sin(angle))).normalize();
                Vec3 motion = tangentDir.scale(speed).add(0, downward, 0);

                projectile.setDeltaMovement(motion);
                world.addFreshEntity(projectile);

                PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                        projectile,
                        new OnCustomProjectileSpawnedPayload(projectile.getId())
                );
            }
        }
    }

    @Override
    public void hitboxTick(SpellEffectAreaEntity hitbox) {
        if (hitbox.level().isClientSide) {
            if (!hitbox.particleEmitted) {
                FX napalmSprout = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(
                        PrismaArcanum.MOD_ID, "napalm_sprout"));
                hitbox.particleEmitted = true;
                new BlockEffectExecutor(napalmSprout, hitbox.level(), hitbox.blockPosition()).start();
            }
            return;
        }

        AABB box = hitbox.getBoundingBox();
        for (Entity e : hitbox.level().getEntities(hitbox, box)) {
            if (e instanceof LivingEntity living && living.isAlive()) {
                // Only add if missing
                if (!living.hasEffect((PASpellEffectRegistry.NAPALM_BURN))) {
                    living.addEffect(new MobEffectInstance(
                            PASpellEffectRegistry.NAPALM_BURN,
                            hitbox.effectDuration, hitbox.amplifier,
                            false, // ambient
                            false, // show vanilla particles
                            true   // show icon
                    ));
                }
            }
        }
    }
}
