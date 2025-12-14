package com.teamsimplyrs.prismaarcanum.api.spell.spells.ignis;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.FireballSpellProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class FireballSpell extends AbstractSpell {
    public static final String spellID = "fireball_spell";
    public static final Element element = Element.Ignis;
    public static final School school = School.Annihilation;

    public static final int tier = 1;
    public static final int basicManaCost = 10;
    public static final int basicCooldown = 60;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 50f;
    private static final float baseSpeed = 1f;

    private static final int maxBounceCount = 3;

    public FireballSpell() {
        super(spellID, element, school, tier, basicManaCost, basicCooldown, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            super.cast(player, world);
            FireballSpellProjectile projectile = new FireballSpellProjectile(player, world, this.getResourceLocation());

            var projectileHeight = projectile.getBoundingBox().getYsize();
            double offsetY = player.getEyeHeight() - (projectileHeight / 2.0);
            Vec3 offset = player.position().add(0, offsetY, 0);

            projectile.setPos(offset);
            projectile.setYRot(player.getYRot());
            projectile.setProjectileParameters(getDamage(), getLifetime(), getVelocity(), getBounceCount(), true, getProjectileMotionType());
            projectile.launch(player.getLookAngle());

            world.addFreshEntity(projectile);
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(projectile, new OnCustomProjectileSpawnedPayload(projectile.getId()));
        }
    }

    public float getDamage() {
        return tier * baseDamage;
    }

    public float getVelocity() {
        return (1 + (tier - 1) * 0.5f) * baseSpeed;
    }

    public int getLifetime() {
        return -1;
    }

    public int getBounceCount() {
        return 3;
    }

    public boolean shouldBounce() {
        return true;
    }

    public ProjectileMotionType getProjectileMotionType() {
        return ProjectileMotionType.DEFAULT;
    }
}
