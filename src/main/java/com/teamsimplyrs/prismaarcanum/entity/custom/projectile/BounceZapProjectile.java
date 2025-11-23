package com.teamsimplyrs.prismaarcanum.entity.custom.projectile;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;

import java.util.List;

public class BounceZapProjectile extends AbstractSpellProjectile {

    private static final EntityDataAccessor<Integer> SPLIT_COUNT =
            SynchedEntityData.defineId(BounceZapProjectile.class, EntityDataSerializers.INT);

    private static final int MAX_SPLITS = 3;

    public BounceZapProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public BounceZapProjectile(LivingEntity caster, Level level, ResourceLocation spellID) {
        super(PAEntityRegistry.BOUNCEZAP_PROJECTILE.get(), level);
        this.setOwner(caster);
        this.setLevel(level);
        this.setParentSpell(spellID);
        this.setNoGravity(true);

        this.refreshDimensions();
    }

    @Override
    public void launch(Vec3 rot) {
        super.launch(rot);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPLIT_COUNT, MAX_SPLITS);
    }

    public void setSplits(int splits) {
        this.entityData.set(SPLIT_COUNT, splits);
    }

    public int getSplits() {
        return this.entityData.get(SPLIT_COUNT);
    }


    @Override
    public void startLaunchFX(Vec3 rot) {

    }

    @Override
    public void startTrailFX() {
        if (level().isClientSide) {
            FX lightningFX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "lightning_trail"));
            EntityEffectExecutor entityFX = new EntityEffectExecutor(lightningFX, level(), this, EntityEffectExecutor.AutoRotate.NONE);
            entityFX.setOffset(new Vector3f(0f, 0f, 0f));
            entityFX.start();
        }
    }

    @Override
    public void startHitFX() {

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity entity = result.getEntity();
        Entity owner = getOwner();

        if (!(entity instanceof LivingEntity target) || !(owner instanceof LivingEntity livingOwner)) {
            return;
        }

        target.hurt(this.damageSources().magic(), this.damage);
        if(!level().isClientSide) {
            if (!target.hasEffect(PASpellEffectRegistry.ZAPPED)) {
                target.addEffect(new MobEffectInstance(
                        PASpellEffectRegistry.ZAPPED,
                        40,
                        0,
                        false,
                        false,
                        true
                ));
            }
        }

        int splitsRemaining = getSplits();

        if (!level().isClientSide && splitsRemaining > 0) {

            List<LivingEntity> nearbyEnemies = level().getEntitiesOfClass(
                    LivingEntity.class,
                    target.getBoundingBox().inflate(20),
                    e -> e != livingOwner && e != target && e.isAlive()
            );

            for (int i = 0; i < 3 && i < nearbyEnemies.size(); i++) {
                LivingEntity nextTarget = nearbyEnemies.get(i);

                BounceZapProjectile zap = new BounceZapProjectile(livingOwner, level(), this.parentSpellID);
                zap.setPos(target.getX(), target.getEyeY(), target.getZ());

                Vec3 direction = nextTarget.position().subtract(zap.position()).normalize().scale(1.2);
                zap.setDeltaMovement(direction);

                zap.setSplits(splitsRemaining - 1);

                level().addFreshEntity(zap);

                PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                        zap,
                        new OnCustomProjectileSpawnedPayload(zap.getId())
                );
            }
        }

        discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount > 200) discard();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(0.75f, 0.75f);
    }
}
