package com.teamsimplyrs.prismaarcanum.entity.custom.monster;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.network.payload.SeekerSyncPayload;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

public class IgniumLegionnaireEntity extends Monster implements RangedAttackMob {
    public final AnimationState idleAnimState = new AnimationState();
    public final AnimationState attackAnimState1 = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimState1Timeout = 0;
    private int attack1Delay = 0;
    private boolean attack1Scheduled = false;
    private LivingEntity target;

    private static final float ATTACK_1_DAMAGE = 15;
    private static final float ATTACK_1_LIFETIME = 80;
    private static final float ATTACK_1_SPEED = 70f;

    protected static final Logger LOGGER = LogUtils.getLogger();

    @Override
    protected void registerGoals() {
        addBehaviorGoals();
    }

    protected void addBehaviorGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 20f));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 60, 90, 16));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.75d, 12));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
         this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1d, 60));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60d)
                .add(Attributes.ATTACK_DAMAGE, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.25d)
                .add(Attributes.FOLLOW_RANGE, 20d)
                .add(Attributes.ARMOR, 15d);
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
                this.idleAnimationTimeout = 160;
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

    public IgniumLegionnaireEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
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

                }
            }
        }
    }
}
