package com.teamsimplyrs.prismaarcanum.api.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;

/// <summary> Common Physics class that provides plug-and-play functions for various physics-related behaviors. </summary>
public class PhysicsUtils {

    /// <summary>Use for homing projectiles and stuff.</summary>
    /// <returns>Returns the direction vector for homing, by rotating and normalizing the given velocity to point to the target's position, per tick. Constrained by given maximum turn angle per second. </returns>
    public static Vec3 getHomingDirectionVector(Vec3 currentVelocity, Vec3 currentPos, Vec3 targetPos, float maxTurnAnglePerSecond) {
        Vec3 directionToTarget = currentPos.vectorTo(targetPos).normalize();
        Vec3 currentDirection = currentVelocity.normalize();

        if (directionToTarget == currentDirection) {
            return currentVelocity;
        }

        double cosTheta = Math.clamp(currentDirection.dot(directionToTarget), -1, 1);
        double theta = Math.acos(cosTheta);

        float maxTurnAnglePerTick = maxTurnAnglePerSecond / 20.0f;
        double phi = Math.min(theta, maxTurnAnglePerTick);

        Vec3 rotationAxis = currentDirection.cross(directionToTarget);

        // Rodrigues' Rotation formula
        Vec3 finalVelocity =
                currentVelocity.scale(Math.cos(phi))
                        .add(
                                (rotationAxis.cross(currentDirection)).scale(Math.sin(phi))
                        )
                        .add(
                                rotationAxis.scale(rotationAxis.dot(currentDirection) * (1 - Math.cos(phi)))
                        );

        return finalVelocity;
    }

    /// <summary>Initiates a raycast, starting from the eye position of the source entity, towards its look direction.</summary>
    /// <returns>Returns EntityHitResult or BlockHitResult, whichever is closer to the source entity. If neither an entity nor a block was hit, returns null.</returns>
    public static HitResult raycast(LivingEntity sourceEntity, Level level, float range) {
        Vec3 start = sourceEntity.getEyePosition();
        Vec3 look = sourceEntity.getLookAngle();

        Vec3 end = start.add(look.scale(range));

        AABB searchBox = sourceEntity.getBoundingBox()
                .expandTowards(look.scale(range))
                .inflate(1.0);

        // Entity Raycast
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                sourceEntity,
                start,
                end,
                searchBox,
                entity -> !entity.isSpectator() && entity.isPickable(),
                range * range
        );

        // Block Raycast
        ClipContext context = new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.ANY,
                sourceEntity
        );

        BlockHitResult blockHit = level.clip(context);

        HitResult finalHit = null;

        if (entityHit != null && blockHit.getType() != HitResult.Type.MISS) {
            double entityDist = start.distanceToSqr(entityHit.getLocation());
            double blockDist = start.distanceToSqr(blockHit.getLocation());

            finalHit = entityDist <= blockDist ? entityHit : blockHit;
        }
        else if (entityHit != null) {
            finalHit = entityHit;
        }
        else {
            finalHit = blockHit;
        }

        return finalHit;
    }

    public static Vec3 raycastForPosition(LivingEntity sourceEntity, Level level, float range, Vec3 eyeOffset) {
        Vec3 start = sourceEntity.getEyePosition().add(eyeOffset);
        Vec3 look = sourceEntity.getLookAngle();
        Vec3 end = start.add(look.scale(range));

        ClipContext context = new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                sourceEntity
        );

        BlockHitResult blockHit = level.clip(context);

        AABB box = sourceEntity.getBoundingBox()
                .expandTowards(look.scale(range))
                .inflate(1.0);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                sourceEntity,
                start,
                end,
                box,
                e -> !e.isSpectator(),
                range * range
        );

        HitResult finalHit = null;

        if (entityHit != null && blockHit.getType() != HitResult.Type.MISS) {
            double entityDist = start.distanceToSqr(entityHit.getLocation());
            double blockDist = start.distanceToSqr(blockHit.getLocation());
            finalHit = entityDist < blockDist ? entityHit : blockHit;
        }
        else if (entityHit != null) {
            finalHit = entityHit;
        }
        else if (blockHit.getType() != HitResult.Type.MISS) {
            finalHit = blockHit;
        }

        return finalHit != null ? finalHit.getLocation() : end;
    }
}
