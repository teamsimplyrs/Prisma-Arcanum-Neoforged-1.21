package com.teamsimplyrs.prismaarcanum.api.utils;

import net.minecraft.world.phys.Vec3;

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
}
