package com.teamsimplyrs.prismaarcanum.api.spell.spells.ignis;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.NapalmShootBlankProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class NapalmSpraySpell extends AbstractSpell {

    public static final String spellID = "napalm_spray_spell";
    public static final Element element = Element.Ignis;
    public static final School school = School.Annihilation;

    public static final int tier = 1;
    public static final int basicManaCost = 0;
    public static final float basicCooldown = 1f;

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
            double arcRadius = 18.0; // overall width of the arc
            double depthRadius = 3.0; // how far forward the center extends
            double baseSpeed = 0.6;

            for (int i = 0; i < count; i++) {
                NapalmShootBlankProjectile projectile = new NapalmShootBlankProjectile(player, world);

                // Map i -> angle along the arc, from -π/4 (left) to +π/4 (right)
                double angleRange = Math.PI / 4;
                double angle = -angleRange / 2 + angleRange * (i / (double)(count - 1));

                // Arc position: circle with player as center
                Vec3 offset = forward.scale(Math.cos(angle) * depthRadius)
                        .add(right.scale(Math.sin(angle) * arcRadius * 0.5));

                // Slight vertical staggering — left hits first
                double verticalOffset = baseY - (projectile.getBoundingBox().getYsize() / 2.0) + (i * 0.15);

                Vec3 spawnPos = player.position().add(offset).add(0, verticalOffset, 0);
                projectile.setPos(spawnPos);
                projectile.setYRot(player.getYRot());

                // Give slight downward bias, leftmost falls faster
                double progress = (double) i / (count - 1);
                double downward = -0.06 * (1.0 - progress * 0.3);
                double speed = baseSpeed * (1.0 + progress * 0.1);

                // Fire direction = outward tangent along the arc
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

}
