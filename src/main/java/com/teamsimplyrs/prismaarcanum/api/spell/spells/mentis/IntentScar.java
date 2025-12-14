package com.teamsimplyrs.prismaarcanum.api.spell.spells.mentis;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.IntentScarProjectile;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.ManaPelletProjectile;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;

public class IntentScar extends AbstractSpell {
    public static final String spellID = "intent_scar";
    public static final Element element = Element.Mentis;
    public static final School school = School.Occult;

    public static final int tier = 1;
    public static final int basicManaCost = 10;
    public static final int basicCooldown = 50;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 8f;
    private static final float baseSpeed = 0.8f;
    private static final float baseInaccuracy = 1f;
    private static final float baseLifetime = 60f;

    public IntentScar() {
        super(spellID, element, school, tier, basicManaCost, basicCooldown, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            IntentScarProjectile projectile = new IntentScarProjectile(player, world, this.getResourceLocation());
            Vec3 offset = player.position().add(0, player.getEyeHeight() - projectile.getBoundingBox().getYsize() * 0.5F - 0.25f, 0);
            projectile.setProjectileParameters(getDamage(), getLifetime(), getVelocity(), getBounceCount(), shouldBounce(), getProjectileMotionType());
            projectile.setPos(offset);
            projectile.setEffectRotation(player.getLookAngle());
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
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "intent_scar");
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

    public float getLifetime() {
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


    public ProjectileMotionType getProjectileMotionType() {
        return ProjectileMotionType.DEFAULT;
    }
}
