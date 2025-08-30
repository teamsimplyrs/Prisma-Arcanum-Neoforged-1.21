package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.system.utils.ProjectileMotionType;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ManaPelletProjectile extends AbstractSpellProjectile {
    private static final Logger LOGGER = LogUtils.getLogger();

    private float damage;
    private float lifetime;

    public ManaPelletProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public ManaPelletProjectile(LivingEntity caster, Level level) {
        super(PAEntityRegistry.MANA_PELLET_PROJECTILE.get(), level);
        this.setNoGravity(true);
        this.setOwner(caster);
    }

    @Override
    public void setSpellData(float damage, float lifetime, float velocity, ProjectileMotionType motionType) {
        super.setSpellData(damage, lifetime, velocity, motionType);
    }

    @Override
    public void launch(Vec3 rot) {
        super.launch(rot);
    }

    @Override
    protected void moveDefault() {
        super.moveDefault();
    }

    @Override
    protected void moveFacingTrajectory() {
        super.moveFacingTrajectory();
    }

    @Override
    protected void moveWithRandomizedRotation() {
        super.moveWithRandomizedRotation();
    }

    @Override
    protected void moveHomingTarget() {
        super.moveHomingTarget();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onHit(HitResult result) {
        super.onHit(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        LOGGER.info("Mana Pellet Projectile: Entity hit");
        
        Entity entity = result.getEntity();
        if (entity.isAlive()) {
            entity.hurt(this.damageSources().magic(), damage);
        }
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        kill();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
