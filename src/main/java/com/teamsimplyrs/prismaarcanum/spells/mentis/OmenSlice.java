package com.teamsimplyrs.prismaarcanum.spells.mentis;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.OmenSliceProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class OmenSlice extends AbstractSpell {
    public static final String spellID = "omen_slice";
    public static final Element element = Element.Mentis;
    public static final School school = School.Occult;

    public static final int tier = 1;
    public static final int basicManaCost = 10;
    public static final int basicCooldown = 50;
    public static final int startDelay = 0;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 8f;
    private static final float baseSpeed = 0.8f;
    private static final float baseInaccuracy = 1f;
    private static final float baseLifetime = 60f;

    public OmenSlice() {
        super(spellID, element, school, tier, basicManaCost, basicCooldown, startDelay, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            OmenSliceProjectile projectile = new OmenSliceProjectile(player, world, this.getResourceLocation());
            Vec3 offset = player.position().add(0, player.getEyeHeight() - projectile.getBoundingBox().getYsize() * 0.5F - 0.25f, 0);
            projectile.setProjectileParameters(getDamage(), getLifetime(), getVelocity(), getBounceCount(), shouldBounce(), getProjectileMotionType());
            projectile.setPos(offset);
            projectile.launch(player.getLookAngle());

            world.addFreshEntity(projectile);

            PacketDistributor.sendToPlayersTrackingEntityAndSelf(projectile, new OnCustomProjectileSpawnedPayload(projectile.getId()));

            super.cast(player, world);
        }
    }

    @Override
    public void onCastingStarted(Player player, Level world) {
        super.onCastingStarted(player, world);
    }

    @Override
    public void onCastingFinished(Player player, Level world) {
        super.onCastingFinished(player, world);
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "omen_slice");
    }

    public float getSpeed() {
        return ((float)tier / 2f) * baseSpeed;
    }

    public float getDamage() {
        return tier * baseDamage;
    }

    public float getInaccuracy() {
        return (1f / tier) * baseInaccuracy;
    }

    public float getProjectileLifetime() {
        return tier * baseLifetime;
    }

    public float getVelocity() {
        return (1 + (tier - 1) * 0.5f) * baseSpeed;
    }

    public int getBounceCount() {
        return 0;
    }

    public boolean shouldBounce() {
        return false;
    }

    @Override
    public boolean allowsCharging() {
        return true;
    }

    public ProjectileMotionType getProjectileMotionType() {
        return ProjectileMotionType.DEFAULT;
    }
}
