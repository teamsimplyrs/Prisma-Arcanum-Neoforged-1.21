package com.teamsimplyrs.prismaarcanum.api.spell.spells.common;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.PhysicsUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public abstract class AbstractSpellProjectile extends Projectile {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected float damage = 1f;
    protected float lifetime = 100f;
    protected float velocity = 1f;
    protected int bounceCount = 0;
    protected boolean shouldBounce = false;
    protected ResourceLocation parentSpellID;
    protected ProjectileMotionType motionType = ProjectileMotionType.DEFAULT;
    protected LivingEntity target;

    public AbstractSpellProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    // Data that is passed here is supposed to be final data.
    // the projectile itself shouldn't be concerned with calculating
    // the final values based on modifiers and other factors.
    public void setProjectileParameters(float damage, float lifetime, float velocity, int bounceCount, boolean shouldBounce, ProjectileMotionType motionType) {
        this.damage = damage;
        this.lifetime = lifetime;
        this.velocity = velocity;
        this.motionType = motionType;
        this.bounceCount = bounceCount;
        this.shouldBounce = shouldBounce;
    }

    protected void setParentSpell(ResourceLocation spellID) {
        this.parentSpellID = spellID;
    }

    public void setTarget(LivingEntity entity) {
        if (entity.isAlive()) {
            target = entity;
        }
    }

    public void launch(Vec3 rot) {
        Vec3 delta = rot.scale(velocity);
        setDeltaMovement(delta);
    }

    protected void moveDefault() {
        Vec3 delta = getDeltaMovement();
        Vec3 offsetPos = position().add(delta);
        setPos(offsetPos);
        if (!isNoGravity()) {
            Vec3 gravDelta = getDeltaMovement().add(0, - getDefaultGravity(), 0);
            setDeltaMovement(gravDelta);
        }
    }

    protected void moveHomingTarget() {
        if (target != null && target.isAlive() && !target.isInvisible()) {
            Vec3 delta = getDeltaMovement();
            Vec3 newVelocity = PhysicsUtils.getHomingDirectionVector(delta, this.position(), target.position(), 1.0472f).scale(delta.length()); // 1.0472 rad = 60 deg
            setDeltaMovement(newVelocity);
        } else {
            moveDefault();
        }
    }

    public abstract void startLaunchFX(Vec3 rot);

    public abstract void startTrailFX();

    public abstract void startHitFX();

    @Override
    public void tick() {
        super.tick();
        if (lifetime >= 0 && tickCount > lifetime) {
            discard();
            return;
        }

        switch(motionType) {
            case DEFAULT -> moveDefault();
            case HOMING -> moveHomingTarget();
        }

        checkHit();
    }

    protected void checkHit() {
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        HitResult.Type hitType = hitResult.getType();
        if (hitType != HitResult.Type.MISS) {
            onHit(hitResult);
        }
    }

    public int getBounceCount() {
        return bounceCount;
    }

    public boolean shouldBounce() {
        return shouldBounce;
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target != getOwner() && super.canHitEntity(target);
    }

    @Override
    public void onHit(HitResult result) {
        super.onHit(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (shouldBounce) {
            if (bounceCount <= 0) {
                discard();
            }
            else {

                // TO DO: Move this to PhysicsUtils::getReflectedVector
                Vec3 v = getDeltaMovement();
                Direction dir = result.getDirection();
                Vec3 n = new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ());
                double dot = v.dot(n);
                Vec3 scaledNorm = n.scale(2.0 * dot);
                Vec3 vReflected = v.subtract(scaledNorm);

                // movement error correction (if any)
                this.position().add(n.scale(0.01));

                setDeltaMovement(vReflected);
                bounceCount--;
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
