package com.teamsimplyrs.prismaarcanum.api.spell.spells.mentis;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.entity.custom.projectile.ManaPelletProjectile;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCustomProjectileSpawnedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;

public class ManaPellet extends AbstractSpell {
    public static final String spellID = "mana_pellet";
    public static final Element element = Element.Mentis;
    public static final School school = School.Occult;

    public static final int tier = 1;
    public static final int basicManaCost = 3;
    public static final int basicCooldown = 10;
    public static final int spellDelay = 0;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 1.5f;
    private static final float baseSpeed = 0.3f;
    private static final float baseInaccuracy = 1f;
    private static final float baseLifetime = 50f;

    public ManaPellet() {
        super(spellID, element, school, tier, basicManaCost, basicCooldown, spellDelay, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            ManaPelletProjectile projectile = new ManaPelletProjectile(player, world, this.getResourceLocation());
            Vec3 offset = player.position().add(0, player.getEyeHeight() - projectile.getBoundingBox().getYsize() * 0.5F, 0);
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

        FX mentisPulseMini = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "mentis_pulse_mini"));
        EntityEffectExecutor pulseFX = new EntityEffectExecutor(mentisPulseMini, world, player, EntityEffectExecutor.AutoRotate.LOOK);

        Vec3 look = player.getLookAngle();
        double distance = 0.5f;
        float x = (float)(look.x * distance);
        float y = (float)(player.getEyeHeight());
        float z = (float)(look.z * distance);

        pulseFX.setOffset(new Vector3f(x, 0, z));
        pulseFX.start();
    }

    @Override
    public void onCastingFinished(Player player, Level world) {
        super.onCastingFinished(player, world);
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
