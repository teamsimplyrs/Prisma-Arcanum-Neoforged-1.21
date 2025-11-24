package com.teamsimplyrs.prismaarcanum.entity.custom.monster;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.RippleSeekerProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.SeekerSyncPayload;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

public class RippleSeekerEntity extends Monster implements RangedAttackMob {
    public final AnimationState idleAnimState = new AnimationState();
    public final AnimationState attackAnimState1 = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimState1Timeout = 0;
    private int attack1Delay = 0;
    private boolean attack1Scheduled = false;
    private LivingEntity target;

    private static final float ATTACK_1_DAMAGE = 10;
    private static final float ATTACK_1_LIFETIME = 80;
    private static final float ATTACK_1_SPEED = 70f;

    protected static final Logger LOGGER = LogUtils.getLogger();

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(false);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        addBehaviorGoals();
    }

    protected void addBehaviorGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 20f));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 60, 90, 16));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.75d, 12));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this,1f));
       // this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1d, 60));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.5d, 60));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50d)
                .add(Attributes.ATTACK_DAMAGE, 10d)
                .add(Attributes.FLYING_SPEED, 10f)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1d)
                .add(Attributes.MOVEMENT_SPEED, 0.25d)
                .add(Attributes.FOLLOW_RANGE, 20d)
                .add(Attributes.ARMOR, 10d);
    }

    public void setAttackSync(boolean scheduled, int delay) {
        this.attack1Scheduled = scheduled;
        this.attack1Delay = delay;
    }

    private void setupAnimationStates() {
        if (this.attack1Scheduled) {
            if (attackAnimState1Timeout <= 0) {
                this.idleAnimState.stop();
                this.attackAnimState1Timeout = 55;
                LOGGER.info("shoot animation");
                this.attackAnimState1.start(this.tickCount);
            } else {
                --attackAnimState1Timeout;
            }
        }

        else {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = 120;
                this.idleAnimState.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    public RippleSeekerEntity(EntityType<? extends Monster> entityType, Level level) {

        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);

    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        if (attack1Scheduled || attack1Delay > 0) {
            return;
        }
        LOGGER.info("perform ranged attack");
        attack1Scheduled = true;
        attack1Delay = 14; // animation delay

        var payload = new SeekerSyncPayload(
                this.getId(),
                attack1Scheduled,
                attack1Delay
        );

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(this, payload);
        target = livingEntity;
    }

    // TO DO: preferably make an "AnimatedRangedAttackGoal" generic goal that accounts for animations in its own tick method. (and so on for other animated goals)
    @Override
    public void baseTick() {
        super.baseTick();
        if (!level().isClientSide) {
            if (attack1Scheduled && target != null && target.isAlive() && !target.isInvisible()) {
                if (attack1Delay > 0) {
                    --attack1Delay;
                } else {
                    attack1Scheduled = false;

                    RippleSeekerProjectile projectile = new RippleSeekerProjectile(PAEntityRegistry.RIPPLE_SEEKER_PROJECTILE.get(), this.level());
                    projectile.setOwner(this);
                    projectile.setTarget(target);
                    projectile.setProjectileParameters(ATTACK_1_DAMAGE, ATTACK_1_LIFETIME, 1f, 1, true, ProjectileMotionType.DEFAULT);
                    projectile.setPos(this.position().add(0f, 0.5f, 0f));
                    Vec3 shootDirection = target.position().subtract(this.position()).add(0,0.5,0).normalize();
                    projectile.launch(shootDirection);
                    this.level().addFreshEntity(projectile);

                    PacketDistributor.sendToPlayersTrackingEntityAndSelf(projectile, new OnCustomProjectileSpawnedPayload(projectile.getId()));
                }
            }
        }
    }
}
//
//package com.teamsimplyrs.prismaarcanum.entity.custom.monster;
//
//import com.mojang.logging.LogUtils;
//import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
//import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.RippleSeekerProjectile;
//import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
//import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.world.entity.AnimationState;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.ai.control.FlyingMoveControl;
//import net.minecraft.world.entity.ai.goal.*;
//        import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
//import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.animal.IronGolem;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.monster.RangedAttackMob;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.neoforge.network.PacketDistributor;
//import org.slf4j.Logger;
//
//public class RippleSeekerEntity extends Monster implements RangedAttackMob {
//    public final AnimationState idleAnimState = new AnimationState();
//    public final AnimationState attackAnimState1 = new AnimationState();
//    private int idleAnimationTimeout = 0;
//    private int attackAnimState1Timeout = 0;
//    private int attack1Delay = 0;
//    private boolean attack1Scheduled = false;
//    private LivingEntity target;
//
//    private static final float ATTACK_1_DAMAGE = 10;
//    private static final float ATTACK_1_LIFETIME = 80;
//    private static final float ATTACK_1_SPEED = 70f;
//
//    private static final EntityDataAccessor<Boolean> ATTACK_SCHEDULED =
//            SynchedEntityData.defineId(RippleSeekerEntity.class, EntityDataSerializers.BOOLEAN);
//
//    private static final EntityDataAccessor<Integer> ATTACK_DELAY =
//            SynchedEntityData.defineId(RippleSeekerEntity.class, EntityDataSerializers.INT);
//
//    protected static final Logger LOGGER = LogUtils.getLogger();
//
//    @Override
//    protected PathNavigation createNavigation(Level level) {
//        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
//        flyingpathnavigation.setCanOpenDoors(false);
//        flyingpathnavigation.setCanFloat(true);
//        flyingpathnavigation.setCanPassDoors(false);
//        return flyingpathnavigation;
//    }
//
//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new FloatGoal(this));
//        addBehaviorGoals();
//    }
//
//    protected void addBehaviorGoals() {
//        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
//        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
//        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 20f));
//        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 60, 90, 16));
//        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.75d, 12));
//        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
//        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this,1f));
//        // this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1d, 60));
//        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.5d, 60));
//    }
//
//    public static AttributeSupplier.Builder createAttributes() {
//        return Monster.createMonsterAttributes()
//                .add(Attributes.MAX_HEALTH, 50d)
//                .add(Attributes.ATTACK_DAMAGE, 10d)
//                .add(Attributes.FLYING_SPEED, 10f)
//                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1d)
//                .add(Attributes.MOVEMENT_SPEED, 0.25d)
//                .add(Attributes.FOLLOW_RANGE, 20d)
//                .add(Attributes.ARMOR, 10d);
//    }
//
//    @Override
//    protected void defineSynchedData(SynchedEntityData.Builder builder) {
//        super.defineSynchedData(builder);
//        builder.define(ATTACK_SCHEDULED, false);
//        builder.define(ATTACK_DELAY, 0);
//    }
//
//    private void setupAnimationStates() {
//        if (entityData.get(ATTACK_SCHEDULED)) {
//            if (attackAnimState1Timeout <= 0) {
//                this.idleAnimState.stop();
//                this.attackAnimState1Timeout = 55;
//                this.attackAnimState1.start(this.tickCount);
//            } else {
//                --attackAnimState1Timeout;
//            }
//        }
//
//        else {
//            if (this.idleAnimationTimeout <= 0) {
//                this.idleAnimationTimeout = 120;
//                this.idleAnimState.start(this.tickCount);
//            } else {
//                --this.idleAnimationTimeout;
//            }
//        }
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        if (this.level().isClientSide) {
//            this.setupAnimationStates();
//        }
//    }
//
//    @Override
//    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
//    }
//
//    public RippleSeekerEntity(EntityType<? extends Monster> entityType, Level level) {
//
//        super(entityType, level);
//        this.moveControl = new FlyingMoveControl(this, 10, true);
//
//    }
//
//    @Override
//    public void performRangedAttack(LivingEntity livingEntity, float v) {
//
//        if (entityData.get(ATTACK_SCHEDULED) || entityData.get(ATTACK_DELAY) > 0) {
//            return;
//        }
//
//        entityData.set(ATTACK_SCHEDULED,true);
//        entityData.set(ATTACK_DELAY,14); // animation delay
//
//        target = livingEntity;
//    }
//
//    // TO DO: preferably make an "AnimatedRangedAttackGoal" generic goal that accounts for animations in its own tick method. (and so on for other animated goals)
//    @Override
//    public void baseTick() {
//        super.baseTick();
//        if (!level().isClientSide) {
//            if (entityData.get(ATTACK_SCHEDULED) && target != null && target.isAlive() && !target.isInvisible()) {
//                if (entityData.get(ATTACK_DELAY) > 0) {
//                    entityData.set(ATTACK_DELAY, entityData.get(ATTACK_DELAY)-1);
//                } else {
//                    entityData.set(ATTACK_SCHEDULED,false);
//
//                    RippleSeekerProjectile projectile = new RippleSeekerProjectile(PAEntityRegistry.RIPPLE_SEEKER_PROJECTILE.get(), this.level());
//                    projectile.setOwner(this);
//                    projectile.setTarget(target);
//                    projectile.setProjectileParameters(ATTACK_1_DAMAGE, ATTACK_1_LIFETIME, 1f, 1, true, ProjectileMotionType.DEFAULT);
//                    projectile.setPos(this.position().add(0f, 0.5f, 0f));
//                    Vec3 shootDirection = target.position().subtract(this.position()).add(0,0.5,0).normalize();
//                    projectile.launch(shootDirection);
//                    this.level().addFreshEntity(projectile);
//
//                    PacketDistributor.sendToPlayersTrackingEntityAndSelf(projectile, new OnCustomProjectileSpawnedPayload(projectile.getId()));
//                }
//            }
//        }
//    }
//}
