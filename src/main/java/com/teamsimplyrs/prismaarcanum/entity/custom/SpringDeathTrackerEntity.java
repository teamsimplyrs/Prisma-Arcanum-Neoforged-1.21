package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.network.payload.OnFXAtPositionPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringDeathTrackerEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_TIMER =
            SynchedEntityData.defineId(SpringDeathTrackerEntity.class, EntityDataSerializers.INT);

    private final List<LivingEntity> trackedTargets = new ArrayList<>();
    private final Map<LivingEntity, Vec3> basePositions = new HashMap<>();

    private static final int WAIT_DURATION = 100;  // <-- New wait phase
    private static final int LAUNCH_DURATION = 10;
    private static final int HOVER_DURATION = 90;
    private static final int TOTAL_TIME = WAIT_DURATION + LAUNCH_DURATION + HOVER_DURATION;

    private boolean launchTriggered = false;

    protected final Logger LOGGER = LogUtils.getLogger();

    public SpringDeathTrackerEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.setInvisible(true);
    }

    public void addTargets(List<LivingEntity> list) {
        trackedTargets.clear();
        trackedTargets.addAll(list);

        // record original positions
        for (LivingEntity living : trackedTargets) {
            basePositions.put(living, living.position());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TIMER, TOTAL_TIME);
    }

    private void triggerEffect(List<LivingEntity> entities) {
        LOGGER.info("Trigger effect called");

        for (LivingEntity entity : entities) {
            // Block directly under the mob’s feet
            BlockPos groundPos = entity.blockPosition().below();

            ResourceLocation fxID = ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID, "springdeath"
            );

            PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                    entity,
                    new OnFXAtPositionPayload(groundPos, fxID)
            );
        }
    }

    @Override
    public void tick() {
        super.tick();
       // LOGGER.info(String.valueOf(level().isClientSide));
        if (level().isClientSide) return;

        int timer = entityData.get(DATA_TIMER);
        entityData.set(DATA_TIMER, timer - 1);

        float t = (TOTAL_TIME - timer); // elapsed time since spawned

        for (LivingEntity entity : trackedTargets) {
            if (!entity.isAlive()) continue;

            Vec3 base = basePositions.get(entity);
            if (base == null) continue;

            if (t <= WAIT_DURATION) {
                // During wait: freeze them in original spot
                teleportAndFreeze(entity, base);
            }
            else if (t <= WAIT_DURATION + LAUNCH_DURATION) {
                // launch phase

                if (!launchTriggered) {
                    launchTriggered = true;
                    // Tell clients to spawn FX for each tracked target
                    triggerEffect(trackedTargets);
                }

                double launchT = (t - WAIT_DURATION) / LAUNCH_DURATION;
                double height = 4.0 * Math.sin(launchT * Math.PI / 2.0);
                teleportAndFreeze(entity, base.add(0, height, 0));
            }
            else if (t <= TOTAL_TIME) {
                // hover + wavy float
                double wobble = Math.sin(entity.tickCount * 0.4) * 0.5;
                double hoverHeight = 4.0 + wobble;
                teleportAndFreeze(entity, base.add(0, hoverHeight, 0));

                // periodic damage
                if (entity.tickCount % 20 == 0) {
                    entity.hurt(entity.damageSources().magic(), 1.0F);
                }
            }
        }

        // End condition
        if (timer - 1 <= 0) {
            releaseEntities();
            discard();
        }
    }

    private void teleportAndFreeze(LivingEntity entity, Vec3 pos) {
        entity.teleportTo(pos.x, pos.y, pos.z);
        entity.setDeltaMovement(Vec3.ZERO);  // zero momentum fights physics
        entity.setNoGravity(true);
        if(entity instanceof Mob mob){
            mob.getNavigation().stop();       // stop AI pathing
        }
    }

    private void releaseEntities() {
        for (LivingEntity entity : trackedTargets) {
            if (!entity.isAlive()) continue;
            entity.setNoGravity(false);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Timer", entityData.get(DATA_TIMER));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(DATA_TIMER, tag.getInt("Timer"));
    }
}
