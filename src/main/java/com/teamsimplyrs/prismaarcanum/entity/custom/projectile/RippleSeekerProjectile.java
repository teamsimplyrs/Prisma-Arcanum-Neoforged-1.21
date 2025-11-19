package com.teamsimplyrs.prismaarcanum.entity.custom.projectile;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.api.utils.PhysicsUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RippleSeekerProjectile extends AbstractSpellProjectile {

    public RippleSeekerProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void startLaunchFX(Vec3 rot) {

    }

    @Override
    public void startTrailFX() {

    }

    @Override
    public void startHitFX() {

    }

}
