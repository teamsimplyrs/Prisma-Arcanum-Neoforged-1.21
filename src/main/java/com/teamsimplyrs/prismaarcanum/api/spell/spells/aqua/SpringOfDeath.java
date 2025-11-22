package com.teamsimplyrs.prismaarcanum.api.spell.spells.aqua;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpringDeathTrackerEntity;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpringOfDeath extends AbstractSpell {
    public static final String spellID = "spring_of_death";
    public static final Element element = Element.Aqua;
    public static final School school = School.Annihilation;

    public static final int tier = 2;
    public static final int basicManaCost = 0;
    public static final int basicCooldown = 1;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 50f;
    private static final float baseSpeed = 2f;

    public SpringOfDeath() {
        super(spellID,element,school,tier, basicManaCost,basicCooldown,hasEvolution);
    }

    @Override
    public void onCastingStarted(Player player, Level world) {
        super.onCastingStarted(player, world);

        FX springDeathCircle = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "springdeathcircle"));
        EntityEffectExecutor executor = new EntityEffectExecutor(springDeathCircle, world, player, EntityEffectExecutor.AutoRotate.NONE);

        Vec3 look = player.getLookAngle();
        double distance = 0.5f;
        float x = (float)(look.x * distance);
        float y = (float)(player.getEyeHeight());
        float z = (float)(look.z * distance);

        //executor.setOffset(new Vector3f(x, 0, z));
        executor.start();
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (world.isClientSide) return;
        super.cast(player, world);

        // Collect nearby living entities
        List<LivingEntity> targets = world.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(20),
                e -> e.isAlive() && e != player
        );

        // Spawn our tracker entity with the collected targets
        SpringDeathTrackerEntity tracker = new SpringDeathTrackerEntity(
                PAEntityRegistry.SPRING_DEATH_TRACKER.get(), world
        );

        tracker.addTargets(targets);

        tracker.setPos(player.position());
        world.addFreshEntity(tracker);
    }

}
