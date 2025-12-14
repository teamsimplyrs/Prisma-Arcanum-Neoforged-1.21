package com.teamsimplyrs.prismaarcanum.api.spell.spells.common;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
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

import java.util.List;

public abstract class AbstractSpellProjectile extends Projectile {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected float damage = 1f;
    protected float lifetime = 100f;
    protected float velocity = 1f;
    protected float inaccuracy = 1f;
    protected int bounceCount = 0;
    protected boolean shouldBounce = false;
    protected ResourceLocation parentSpellID;
    protected ProjectileMotionType motionType = ProjectileMotionType.DEFAULT;
    protected LivingEntity target;
    protected Vec3 effectRotation;

    protected EntityEffectExecutor activeTrailingEffect;

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

    public void setEffectRotation(Vec3 rot) {
        effectRotation = rot;
    }

    public void launch(Vec3 rot) {
        Entity owner = this.getOwner();
        if (owner != null) {
            this.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0f, this.velocity, getInaccuracy());
            Vec3 delta = rot.scale(velocity);
            setDeltaMovement(delta);
        }
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

    protected abstract ResourceLocation getTrailFXid();

    protected Vec3 getEffectRotation() {
//        var owner = getOwner();
//        if (owner != null) {
//            return owner.getLookAngle();
//        }

        Vec3 motion = getDeltaMovement().normalize().scale(-1);

        if (motion.lengthSqr() > 1.0e-5) {
            double horizontalMag = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

            float yaw = (float)(Math.atan2(motion.z, motion.x) * (180F / Math.PI)) - 90F;
            float pitch = (float)(-(Math.atan2(motion.y, horizontalMag) * (180F / Math.PI)));

            return new Vec3(pitch, yaw, 0.0f);
        }

        return Vec3.ZERO;
    }

    @Override
    public void tick() {
        super.tick();
        if (lifetime >= 0 && tickCount > lifetime) {
            remove(RemovalReason.DISCARDED);
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

    public float getInaccuracy() {
        return inaccuracy;
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
                remove(RemovalReason.DISCARDED);
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

    @Override
    public void remove(RemovalReason reason) {
        ResourceLocation fxId = getTrailFXid();
        try {
            if (fxId != null) {
                var CACHE = EntityEffectExecutor.CACHE;
                List<EntityEffectExecutor> effects = CACHE.get(this);
                if (effects != null && !effects.isEmpty()) {
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
                    if ((CACHE.get(this)).isEmpty()) {
                        CACHE.remove(this);
                    }

                    EntityEffectExecutor.CACHE = CACHE;
                }
            }
        } catch (Exception e) {
            LOGGER.error("[PrismaArcanum:AbstractSpellProjectile] ERROR! {%s}", e);
        }

        super.remove(reason);
    }
}
